package bank.loans.handler.impl;

import bank.accounts.Account;
import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.data.storage.DataStorage;
import bank.loans.Loan;
import bank.loans.LoanStatus;
import bank.loans.handler.LoanHandler;
import bank.loans.impl.BasicLoan;
import bank.loans.impl.builder.LoanBuilder;
import bank.loans.interest.Interest;
import bank.loans.investments.Investment;
import bank.loans.investments.impl.LoanInvestment;
import bank.time.handler.BankTimeHandler;
import bank.transactions.Transaction;
import javafx.util.Pair;

import java.util.Collection;
import java.util.stream.Collectors;

public class BankLoanHandler implements LoanHandler {
    private DataStorage<Transaction> transactions;
    private DataStorage<Loan> loans;
    private DataStorage<Account> customers;
    private BankTimeHandler timeHandler;

    public BankLoanHandler(DataStorage<Transaction> transactions, DataStorage<Loan> loans, DataStorage<Account> customers, BankTimeHandler timeHandler) {
        this.transactions = transactions;
        this.loans = loans;
        this.customers = customers;
        this.timeHandler = timeHandler;
    }

    public BankLoanHandler(DataStorage<Transaction> transactions, DataStorage<Loan> loans, DataStorage<Account> customers) {
        this.transactions = transactions;
        this.loans = loans;
        this.customers = customers;
    }

    public BankLoanHandler(DataStorage<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public Loan createLoan(LoanBuilder loadDetails, Interest interest) {
        return new BasicLoan(loadDetails, interest);
    }

    @Override
    public void addInvestment(Loan loan, Investment investment, Account srcAcc) throws NonPositiveAmountException, NoMoneyException {
        float amount = investment.getBaseAmount();
        loan.addInvestment(investment);
        transactions.addData(loan.getLoanAccount().deposit(amount, "Loan"));
        transactions.addData(srcAcc.withdraw(amount, "Loan"));

        checkLoanStatus(loan, srcAcc);
    }

    private void checkLoanStatus(Loan loan, Account srcAcc) throws NoMoneyException, NonPositiveAmountException {
        float loanAmount = loan.getBaseAmount();
        float accountBalance = srcAcc.getBalance();
        Account requester;

        if(loanAmount == accountBalance) {
            requester = customers.getDataById(loan.getOwnerId());
            srcAcc.withdraw(loanAmount, "Loan started");
            requester.deposit(loanAmount, "Loan started");
            loan.setStatus(LoanStatus.ACTIVE);
        }
    }

    public Investment createInvestment(int investorId, Interest interest, int duration) {
        return new LoanInvestment(investorId, interest, duration);
    }

    public void oneCycle() {
        Collection<Pair<Loan, Integer>> loanCollection = loans.getAllPairs();
        Collection<Pair<Loan, Integer>> filteredLoans = loanCollection.stream()
                .filter((loan -> (loan.getKey().getStatus() != LoanStatus.FINISHED) && (loan.getKey().getStatus() != LoanStatus.PENDING)))
                .collect(Collectors.toList());

        for(Pair<Loan, Integer> loanPair : filteredLoans) {
            makePayment(loanPair.getKey());
        }
    }

    private void makePayment(Loan loan) {
        Account srcAcc = customers.getDataById(loan.getOwnerId());
        float payment = loan.getCyclePayment();
        try {
            // TODO : fix for (based on how loans work)
            transactions.addData(srcAcc.withdraw(payment, "Loan Cycle"));
            Collection<Investment> investments = loan.getInvestments();
            boolean isFinished = true;

            for(Investment investment : investments) {
                Account investor = customers.getDataById(investment.getInvestorId());
                transactions.addData(investor.deposit(investment.getPayment(), "Loan Cycle"));
                investment.payment();
                if(!investment.isFullyPaid())
                    isFinished = false;
            }

            if(isFinished) {
                loan.setStatus(LoanStatus.FINISHED);
            }

        } catch (NonPositiveAmountException e) {
            e.printStackTrace();
        } catch (NoMoneyException e) {
            loan.setStatus(LoanStatus.RISK);
        }
    }

    public void deriskLoan(Loan loan) throws NoMoneyException, NonPositiveAmountException {
        float amount = getDeriskAmount(loan);
        Account srcAcc = customers.getDataById(loan.getOwnerId());

        transactions.addData(srcAcc.withdraw(amount, "Derisk Loan"));
        loan.setStatus(LoanStatus.ACTIVE);

        int times = (int) (amount / loan.getCyclePayment());

        for(int i = 0; i < times; i++) {
            makePayment(loan);
        }
    }

    public float getDeriskAmount(Loan loan) {
        Investment investment = loan.getInvestments().get(0);
        int startingYaz = loan.getStartingYaz();
        int cycles = timeHandler.getCurrentTime() - startingYaz;

        return getMissingCycles(investment, cycles) * loan.getCyclePayment();
//        loan.getStartingYaz();


    }

    private int getMissingCycles(Investment investment, int cycles) {
        return (int) (((investment.getPayment() * cycles) - (investment.getAmountPaid()))
                                / (investment.getPayment()));
    }

    @Override
    public void printAllLoans() {
        System.out.println(loans);
    }

}
