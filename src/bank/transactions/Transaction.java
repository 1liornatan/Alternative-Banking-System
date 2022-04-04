package bank.transactions;

import bank.data.Singular;

public interface Transaction extends Singular {

    String getId();
    float getAmount();
    String getDescription();
    String getAccountId();
}
