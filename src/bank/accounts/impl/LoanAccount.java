package bank.accounts.impl;

import bank.accounts.Account;
import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.data.Singular;
import bank.transactions.Transaction;
import bank.transactions.impl.BasicTransaction;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LoanAccount implements Account, Singular {
    private static Integer idGenerator = 60000;
    private final String id;
    private float balance;
    private final Set<String> transactions;

    public LoanAccount() {
        id = (idGenerator++).toString();
        balance = 0;
        transactions = new HashSet<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public float getBalance() {
        return balance;
    }

    public Set<String> getTransactions() {
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
    public Transaction deposit(float amount, String description) throws NonPositiveAmountException {
        if(amount <= 0) throw new NonPositiveAmountException();

        Transaction transaction = new BasicTransaction(amount, description);
        transactions.add(transaction.getId());
        balance += amount;

        return transaction;
    }
    @Override
    public Transaction withdraw(float amount, String description) throws NonPositiveAmountException, NoMoneyException {
        if(amount <= 0) throw new NonPositiveAmountException();
        if(amount > balance) throw new NoMoneyException();

        Transaction transaction = new BasicTransaction(amount, description);
        transactions.add(transaction.getId());
        balance -= amount;

        return transaction;
    }
}
