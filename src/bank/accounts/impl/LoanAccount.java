package bank.accounts.impl;

import bank.accounts.Account;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.data.Singular;
import bank.transactions.Transaction;
import bank.transactions.impl.BasicTransaction;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LoanAccount implements Account, Singular {
    private static int idGenerator = 1;
    private int id;
    private float balance;
    private Set<Integer> transactions;

    public LoanAccount() {
        id = idGenerator++;
        balance = 0;
        transactions = new HashSet<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public float getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanAccount that = (LoanAccount) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Set<Integer> getTransactions() {
        return transactions;
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
    public Transaction withdraw(float amount, String description) throws NonPositiveAmountException {
        if(amount <= 0) throw new NonPositiveAmountException();

        Transaction transaction = new BasicTransaction(amount, description);
        transactions.add(transaction.getId());
        balance -= amount;

        return transaction;
    }
}
