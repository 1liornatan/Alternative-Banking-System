package bank.account.impl;

import bank.account.Accounts;
import bank.account.impl.exceptions.NegativeAmountException;

import java.util.Objects;

public class CustomerAccount implements Accounts {
    private static int idGenerator = 1;
    private int id;
    private String name;
    private float balance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerAccount that = (CustomerAccount) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    CustomerAccount(String name, float balance) {
        id = idGenerator++;
        this.name = name;
        this.balance = balance;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public float getBalance() {
        return balance;
    }

    public void deposit(float amount) throws NegativeAmountException {
        if(amount <= 0) throw new NegativeAmountException();

        balance += amount;
        // TODO: CREATE TRANSACTION
    }

    public void withdraw(float amount) throws NegativeAmountException {
        if(amount <= 0) throw new NegativeAmountException();

        balance += amount;
        // TODO: CREATE TRANSACTION
    }
}
