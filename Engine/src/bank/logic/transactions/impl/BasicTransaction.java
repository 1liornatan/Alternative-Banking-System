package bank.logic.transactions.impl;

import bank.logic.transactions.Transaction;

public class BasicTransaction implements Transaction {
    private static Integer idGenerator = 40000;
    private final String id;
    private final String description;
    private final int amount, previousBalance;

    public BasicTransaction(int amount, String description, int previousBalance) {
        this.previousBalance = previousBalance;
        id = (idGenerator++).toString();
        this.amount = amount;
        this.description = description;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Transaction: '" + description +
                ", Amount: " + (amount >= 0 ? "+":"") + amount +
                ", Balance before: " + previousBalance +
                ", Balance after: " + (previousBalance + amount);
    }

    @Override
    public int getPreviousBalance() {
        return previousBalance;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getAmount() {
        return amount;
    }
}
