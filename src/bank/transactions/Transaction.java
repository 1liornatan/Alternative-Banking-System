package bank.transactions;

import bank.data.Singular;

public interface Transaction extends Singular {

    String getId();
    int getAmount();

    int getPreviousBalance();

    String getDescription();
}
