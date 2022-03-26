package bank.loans.accounts.impl;

import bank.loans.accounts.Account;
import bank.loans.accounts.impl.exceptions.NonPositiveAmountException;
import bank.data.Singular;

import java.util.Objects;

public class LoanAccount implements Account, Singular {
    private static int idGenerator = 1;
    private int id;
    private float balance;

    public LoanAccount() {
        id = idGenerator++;
        balance = 0;
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

    @Override
    public void deposit(float amount) throws NonPositiveAmountException {
        if(amount <= 0) throw new NonPositiveAmountException();

        balance += amount;
        // TODO: CREATE TRANSACTION
    }
    @Override
    public void withdraw(float amount) throws NonPositiveAmountException {
        if(amount <= 0) throw new NonPositiveAmountException();

        balance -= amount;
        // TODO: CREATE TRANSACTION
    }
}
