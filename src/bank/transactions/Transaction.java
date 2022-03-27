package bank.transactions;

import bank.data.Singular;

public interface Transaction extends Singular {

    int getId();
    float getAmount();
    String getDescription();
    int getAccountId();
}
