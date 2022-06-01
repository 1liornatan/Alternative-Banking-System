package bank.loans.handler.impl;

import bank.Bank;
import bank.accounts.Account;
import bank.accounts.CustomerAccount;
import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.data.storage.DataStorage;
import bank.impl.exceptions.DataNotFoundException;
import bank.loans.Loan;
import bank.loans.LoanStatus;
import bank.loans.handler.LoanHandler;
import bank.loans.impl.BasicLoan;
import bank.loans.impl.builder.LoanBuilder;
import bank.loans.interest.Interest;
import bank.loans.investments.Investment;
import bank.messages.impl.BankNotification;
import bank.time.TimeHandler;
import bank.transactions.Transaction;
import javafx.util.Pair;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BankLoanHandler implements LoanHandler {
    private final DataStorage<Transaction> transactions;
    private DataStorage<Loan> loans;
    private DataStorage<CustomerAccount> customers;
    private TimeHandler timeHandler;

    public BankLoanHandler(DataStorage<Transaction> transactions, DataStorage<Loan> loans, DataStorage<CustomerAccount> customers, TimeHandler timeHandler) {
        this.transactions = transactions;
        this.loans = loans;
        this.customers = customers;
        this.timeHandler = timeHandler;
    }

    public BankLoanHandler(DataStorage<Transaction> transactions, DataStorage<Loan> loans, DataStorage<CustomerAccount> customers) {
        this.transactions = transactions;
        this.loans = loans;
        this.customers = customers;
    }

    public BankLoanHandler(DataStorage<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public Loan createLoan(LoanBuilder loadDetails, Interest interest) {
        return new BasicLoan(loadDetails, interest,timeHandler);
    }

    @Override
    public void addInvestment(Loan loan, Investment investment, CustomerAccount srcAcc) throws NonPositiveAmountException, NoMoneyException, DataNotFoundException {
        int amount = investment.getBaseAmount();
        loan.addInvestment(investment);
        transactions.addData(loan.getLoanAccount().deposit(amount, "Loan"));
        transactions.addData(srcAcc.withdraw(amount, "Loan"));
        srcAcc.addInvestedLoan(loan);

        checkLoanStatus(loan);
    }

    @Override
    public void payLoanByAmount(Loan loan, int amount) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException {
        CustomerAccount srcAcc = customers.getDataById(loan.getOwnerId());

        transactions.addData(srcAcc.withdraw(amount, "Loan '" + loan.getId() + "' Debt Payment"));
        customers.getDataById(srcAcc.getId()).addNotification(new BankNotification("Payment of " + amount + " for loan '" + loan.getId() + "'",timeHandler.getCurrentTime()));
        //int missingCycles = (loan.getCurrentPayment() - loan.getFullPaidCycles());
        int cyclesToPay = amount / loan.getPayment();
        for(int i = 0; i < cyclesToPay; i++)
            loan.fullPaymentCycle();

        List<Investment> investments = loan.getInvestments();
        // int cycles = (timeHandler.getCurrentTime() - loan.getStartedYaz()) / loan.getCyclesPerPayment();

        for(Investment investment : investments) {
            for(int i = 0; i < cyclesToPay; i++) {
                investmentPayment(investment);
            }
        }

        if(loan.getAmountToCloseLoan() == 0) {
            loan.setStatus(LoanStatus.FINISHED);
            srcAcc.addNotification(new BankNotification("Loan '" + loan.getId() + "' is now finished", timeHandler.getCurrentTime()));
            loan.setFinishedYaz(timeHandler.getCurrentTime());
        }
    }

    private void checkLoanStatus(Loan loan) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException {
        int loanAmount = loan.getBaseAmount();
        Account loanAccount = loan.getLoanAccount();
        int accountBalance = loanAccount.getBalance();

        Account requester;

        if(loanAmount == loan.getLoanAccount().getBalance()) {
            requester = customers.getDataById(loan.getOwnerId());
            transactions.addData(loanAccount.withdraw(loanAmount, "Loan started"));
            transactions.addData(requester.deposit(loanAmount, "Loan started"));
            loan.setPayments();
            loan.setStatus(LoanStatus.ACTIVE);
            loan.setStartedYaz(timeHandler.getCurrentTime());
        }
        else if(loan.getStatus() == LoanStatus.NEW) {
            loan.setStatus(LoanStatus.PENDING);
        }
    }


    public void oneCycle() throws DataNotFoundException, NonPositiveAmountException {
        Collection<Pair<Loan, Integer>> loanCollection = loans.getAllPairs();
        Collection<Pair<Loan, Integer>> filteredLoans = loanCollection.stream()
                .filter((loan -> (loan.getKey().getStatus() == LoanStatus.RISKED) || (loan.getKey().getStatus() == LoanStatus.ACTIVE)))
                .filter((loan -> ((timeHandler.getCurrentTime() % loan.getKey().getCyclesPerPayment()) == 0)))
                .collect(Collectors.toList());

        for(Pair<Loan, Integer> loanPair : filteredLoans) {
            makePayment(loanPair.getKey());
        }

    }

    private void makePayment(Loan loan) throws DataNotFoundException, NonPositiveAmountException {
        CustomerAccount srcAcc = customers.getDataById(loan.getOwnerId());
        int payment = loan.getPayment();
        try {
            transactions.addData(srcAcc.withdraw(payment, "Loan Cycle"));
            srcAcc.addNotification(new BankNotification("Loan Cycle", timeHandler.getCurrentTime()));
            loan.fullPaymentCycle();
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
                loan.setFinishedYaz(timeHandler.getCurrentTime());
            }

        } catch (NonPositiveAmountException e) {
            e.printStackTrace();
        } catch (NoMoneyException e) {
            loan.setStatus(LoanStatus.RISKED);
            addRiskedNotification(loan);
            partiallyPayment(loan);
        } finally {
            loan.nextPayment();
        }
    }

    private void addRiskedNotification(Loan loan) throws DataNotFoundException {
        for(Investment investment : loan.getInvestments()) {
            customers.getDataById(investment.getInvestorId()).addNotification(new BankNotification("Loan '" +
                    loan.getId() + "' is in Risk", timeHandler.getCurrentTime()));;
        }
    }

    private void investmentPayment(Investment investment) throws DataNotFoundException, NonPositiveAmountException {
        Account investor = customers.getDataById(investment.getInvestorId());
        transactions.addData(investor.deposit(investment.getPayment(), "Loan Payment Cycle of '" + investment.getLoanId() + "'"));
        investment.payment();
    }

    private void partiallyPayment(Loan loan) throws DataNotFoundException, NonPositiveAmountException {
        Account srcAcc = customers.getDataById(loan.getOwnerId());
        int accountBalance = srcAcc.getBalance();
        int sumPaid = 0;

        List<Investment> sortedInvestors = loan.getInvestments().stream()
                .sorted(Comparator.comparingInt(Investment::getPayment))
                .collect(Collectors.toList());

        for(Investment investment : sortedInvestors) {
            if(!investment.isFullyPaid() && investment.getPayment() < (accountBalance - sumPaid)) {
                sumPaid += investment.getPayment();
                investmentPayment(investment);
            }
            else
                break;
        }

    }

    public void deriskLoan(Loan loan) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException {
        int amount = loan.getDeriskAmount();
        Account srcAcc = customers.getDataById(loan.getOwnerId());

        transactions.addData(srcAcc.withdraw(amount, "Derisk Loan"));
        customers.getDataById(srcAcc.getId()).addNotification(new BankNotification("Derisk Loan",timeHandler.getCurrentTime()));
        int missingCycles = (loan.getCurrentPayment() - loan.getFullPaidCycles());

        for(int i = 0; i < missingCycles; i++)
            loan.fullPaymentCycle();

        loan.setStatus(LoanStatus.ACTIVE);

        List<Investment> investments = loan.getInvestments();
        int cycles = (timeHandler.getCurrentTime() - loan.getStartedYaz()) / loan.getCyclesPerPayment();

        for(Investment investment : investments) {
            for(int i = investment.getPaymentsReceived(); i < cycles; i++) {
                investmentPayment(investment);
            }
        }
    }

}
