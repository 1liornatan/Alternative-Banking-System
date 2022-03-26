package bank.loans.accounts;

import bank.loans.accounts.impl.exceptions.NonPositiveAmountException;

/* An interface for all kind of accounts
   with the default actions required
 */
public interface Account {

    int getId();

    float getBalance();

    void deposit(float amount) throws NonPositiveAmountException;

    void withdraw(float amount) throws NonPositiveAmountException;

}
