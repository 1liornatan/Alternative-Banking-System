package bank.account;

import bank.account.impl.exceptions.NegativeAmountException;

/* An interface for all kind of accounts
   with the default actions required
 */
public interface Accounts {

    public int getId();

    public float getBalance();

    public void deposit(float amount) throws NegativeAmountException;

    public void withdraw(float amount);

}
