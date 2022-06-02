package manager.transactions;

public class TransactionDTO {
    final String description;
    final int amount;
    final int previousBalance;
    final int yazMade;

    public TransactionDTO(String description, int amount, int previousBalance, int yazMade) {
        this.description = description;
        this.amount = amount;
        this.previousBalance = previousBalance;
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
