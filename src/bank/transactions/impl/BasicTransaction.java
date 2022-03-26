package bank.transactions.impl;

import bank.transactions.Transaction;

public class BasicTransaction implements Transaction {
    private static int idGenerator = 40000;
    private int id, accountId;
    private String description;
    private float amount;

    public BasicTransaction(float amount, String description) {
        id = idGenerator++;
        this.amount = amount;
        this.description = description;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getAccountId() {
        return accountId;
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
