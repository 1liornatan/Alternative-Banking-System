package bank.loans.handler.impl;

import bank.accounts.Account;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.data.storage.impl.BankDataStorage;
import bank.loans.Loan;
import bank.loans.LoanStatus;
import bank.loans.handler.LoanHandler;
import bank.loans.impl.BasicLoan;
import bank.loans.interest.Interest;
import bank.loans.investments.Investment;
import bank.loans.investments.impl.LoanInvestment;
import bank.transactions.Transaction;

import java.util.Collection;
import java.util.stream.Collectors;

public class BankLoanHandler implements LoanHandler {
    private BankDataStorage<Transaction> transactions;
    private BankDataStorage<Loan> loans;
    private BankDataStorage<Account> customers;

    public BankLoanHandler(BankDataStorage<Transaction> transactions, BankDataStorage<Loan> loans, BankDataStorage<Account> customers) {
        this.transactions = transactions;
        this.loans = loans;
        this.customers = customers;
    }

    public BankLoanHandler(BankDataStorage<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public Loan createLoan(int ownerId, float baseAmount, float interestPercent, String category) {
        Loan loan = new BasicLoan(ownerId, baseAmount, interestPercent, category);

        return loan;
    }

    @Override
    public void addInvestment(Loan loan, Investment investment, Account srcAcc) throws NonPositiveAmountException {
        float amount = investment.getBaseAmount();
        loan.addInvestment(investment);
        transactions.addData(loan.getLoanAccount().deposit(amount, "Loan"));
        transactions.addData(srcAcc.withdraw(amount, "Loan"));
    }

    public Investment createInvestment(int investorId, Interest interest, int duration) {
        Investment loanInvestment = new LoanInvestment(investorId, interest, duration);
        return loanInvestment;
    }

    public void oneCycle() {
        Collection<Loan> loanCollection = loans.getAll();
        Collection<Loan> filteredLoans = loanCollection.stream()
                .filter((loan -> loan.getStatus() != LoanStatus.FINISHED))
                .collect(Collectors.toList());

        for(Loan loan : filteredLoans) {
            makePayment(loan);
            // TODO: CHECK STATUS,
        }
    }

    private void makePayment(Loan loan) {
        Account srcAcc = customers.getDataById(loan.getOwnerId());
        float payment = loan.getCyclePayment();
        try {
            // TODO : add details
            transactions.addData(srcAcc.withdraw(payment, "Loan Cycle"));
            Collection<Investment> investments = loan.getInvestments();
            for(Investment investment : investments) {
                Account investor = customers.getDataById(investment.getInvestorId());
                transactions.addData(investor.deposit(investment.getPayment(), "Loan Cycle"));
                investment.payment();
            }
        } catch (NonPositiveAmountException e) {
            e.printStackTrace();
        }
    }

}
