package manager.transactions;

public class TransactionData {
    private String description;
    private int amount, yazMade, previousBalance;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getYazMade() {
        return yazMade;
    }

    public void setYazMade(int yazMade) {
        this.yazMade = yazMade;
    }

    public int getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(int previousBalance) {
        this.previousBalance = previousBalance;
    }
}
