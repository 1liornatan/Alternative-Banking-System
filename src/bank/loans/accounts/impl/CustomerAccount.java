package bank.loans.accounts.impl;

import bank.loans.accounts.Account;
import bank.loans.accounts.impl.exceptions.NonPositiveAmountException;
import bank.data.Singular;

import java.util.Objects;

public class CustomerAccount implements Account, Singular {
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

    public CustomerAccount(String name, float balance) {
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

    @Override
    public String toString() {
        return "CustomerAccount{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}
