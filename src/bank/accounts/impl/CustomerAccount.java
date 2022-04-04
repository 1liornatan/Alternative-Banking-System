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
    private float balance;
    private final List<String> transactions;

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

    public CustomerAccount(String name, float balance) {
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    @Override
    public String getId() {
        return name;
    }


    @Override
    public float getBalance() {
        return balance;
    }

    @Override
    public Transaction deposit(float amount, String description) throws NonPositiveAmountException {
        if(amount <= 0) throw new NonPositiveAmountException();

        Transaction transaction = new BasicTransaction(amount, description);
        transactions.add(transaction.getId());
        balance += amount;

        return transaction;
    }
    @Override
    public Transaction withdraw(float amount, String description) throws NonPositiveAmountException, NoMoneyException {
        if(amount <= 0) throw new NonPositiveAmountException(); // TODO: Not enough money Exception.
        if(amount > balance) throw new NoMoneyException();

        Transaction transaction = new BasicTransaction(amount, description);
        transactions.add(transaction.getId());
        balance -= amount;

        return transaction;
    }

    @Override
    public String toString() {
        return "Account Name: " + name +
                "\nCurrent Balance: " + balance;
    }

}
