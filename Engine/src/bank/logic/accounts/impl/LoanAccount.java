package bank.logic.accounts.impl;

import bank.logic.accounts.Account;
import bank.logic.accounts.impl.exceptions.NoMoneyException;
import bank.logic.accounts.impl.exceptions.NonPositiveAmountException;
import bank.logic.data.Singular;
import bank.logic.transactions.Transaction;
import bank.logic.transactions.impl.BasicTransaction;

import java.util.*;

public class LoanAccount implements Account, Singular {
    private static Integer idGenerator = 60000;
    private final String id;
    private int balance;
    private final List<Transaction> transactions;

    public LoanAccount(int balance) {
        id = (idGenerator++).toString();
        this.balance = balance;
        transactions = new ArrayList<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanAccount that = (LoanAccount) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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

        Transaction transaction = new BasicTransaction(amount * (-1), description, balance);
        transactions.add(transaction);
        balance -= amount;

        return transaction;
    }
}
