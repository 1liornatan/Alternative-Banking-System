package bank.transactions;

public interface Transaction {

    int getId();
    float getAmount();
    String getDescription();
    int getAccountId();
}
