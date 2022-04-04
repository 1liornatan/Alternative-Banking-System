package bank.transactions.impl;

import bank.transactions.Transaction;

public class BasicTransaction implements Transaction {
    private static Integer idGenerator = 40000;
    private final String id;
    private String accountId;
    private final String description;
    private final float amount;

    public BasicTransaction(float amount, String description) {
        id = (idGenerator++).toString();
        this.amount = amount;
        this.description = description;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getAccountId() {
        return accountId;
    }

    @Override
    public String toString() {
        return "(Transaction Name: " + description +
                ", ID: " + id +
                ", Amount: " + amount +
                ')';
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public float getAmount() {
        return amount;
    }
}
