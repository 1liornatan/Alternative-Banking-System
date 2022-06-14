package bank.logic.transactions;

import bank.logic.data.Singular;

public interface Transaction extends Singular {

    String getId();
    int getAmount();

    int getPreviousBalance();

    String getDescription();
}
