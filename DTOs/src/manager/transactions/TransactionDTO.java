package manager.transactions;

import bank.transactions.Transaction;

public class TransactionDTO {
    String description;
    int amount;
    int previousBalance, yazMade;

    public TransactionDTO(Transaction transaction, int yazMade) {
        description = transaction.getDescription();
        amount = transaction.getAmount();
        previousBalance = transaction.getPreviousBalance();
        this.yazMade = yazMade;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public int getPreviousBalance() {
        return previousBalance;
    }

    public int getYazMade() {
        return yazMade;
    }
}
