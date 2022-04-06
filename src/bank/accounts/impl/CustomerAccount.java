package bank.accounts.impl;

import bank.accounts.Account;
import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.data.Singular;
import bank.transactions.Transaction;
import bank.transactions.impl.BasicTransaction;

import java.util.*;

public class CustomerAccount implements Account, Singular {
    private final String name;
    private int balance;
    private final List<String> transactions;
    private List<String> loansRequested, loansInvested;

    @Override
    public List<String> getTransactions() {
        return transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerAccount that = (CustomerAccount) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public CustomerAccount(String name, int balance) {
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>();

        loansRequested = new ArrayList<>();
        loansInvested = new ArrayList<>();
    }

    @Override
    public void addRequestedLoan(String id) {
        loansRequested.add(id);
    }

    @Override
    public void addInvestedLoan(String id) {
        loansInvested.add(id);
    }

    @Override
    public List<String> getLoansRequested() {
        return loansRequested;
    }

    @Override
    public List<String> getLoansInvested() {
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
        transactions.add(transaction.getId());
        balance += amount;

        return transaction;
    }
    @Override
    public Transaction withdraw(int amount, String description) throws NonPositiveAmountException, NoMoneyException {
        if(amount <= 0) throw new NonPositiveAmountException();
        if(amount > balance) throw new NoMoneyException();

        Transaction transaction = new BasicTransaction(amount  * (-1), description, balance);
        transactions.add(transaction.getId());
        balance -= amount;

        return transaction;
    }

    @Override
    public String toString() {
        return "Account Name: " + name +
                ", Current Balance: " + balance;
    }

}
