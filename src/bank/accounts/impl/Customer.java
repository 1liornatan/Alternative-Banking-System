package bank.accounts.impl;

import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.loans.Loan;
import bank.transactions.Transaction;
import bank.transactions.impl.BasicTransaction;

import java.util.*;

public class Customer extends LoanAccount implements bank.accounts.CustomerAccount {
    private final String name;
    private int balance;
    private final List<Transaction> transactions;
    private List<Loan> loansRequested, loansInvested;

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer that = (Customer) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int getNumOfRelatedLoans() {return loansRequested.size();}

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Customer(String name, int balance) {
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>();

        loansRequested = new ArrayList<>();
        loansInvested = new ArrayList<>();
    }

    @Override
    public void addRequestedLoan(Loan loan) {
        loansRequested.add(loan);
    }
    @Override
    public void addInvestedLoan(Loan loan) {
        loansInvested.add(loan);
    }

    @Override
    public List<Loan> getLoansRequested() {
        return loansRequested;
    }

    @Override
    public List<Loan> getLoansInvested() {
        return loansInvested;
    }

    @Override
    public String getId() {
        return name;
    }


    @Override
    public int getBalance() {
        return balance;
    }

    @Override
    public Transaction deposit(int amount, String description) throws NonPositiveAmountException {
        if(amount <= 0) throw new NonPositiveAmountException();

        Transaction transaction = new BasicTransaction(amount, description, balance);
        transactions.add(transaction);
        balance += amount;

        return transaction;
    }
    @Override
    public Transaction withdraw(int amount, String description) throws NonPositiveAmountException, NoMoneyException {
        if(amount <= 0) throw new NonPositiveAmountException();
        if(amount > balance) throw new NoMoneyException();

        Transaction transaction = new BasicTransaction(amount  * (-1), description, balance);
        transactions.add(transaction);
        balance -= amount;

        return transaction;
    }

    @Override
    public String toString() {
        return "Account Name: " + name +
                ", Current Balance: " + balance;
    }

}
