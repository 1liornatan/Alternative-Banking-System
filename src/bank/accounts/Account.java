package bank.accounts;

import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.data.Singular;
import bank.transactions.Transaction;

/* An interface for all kind of accounts
   with the default actions required
 */
public interface Account extends Singular {

    int getId();

    String getName();

    float getBalance();

    Transaction deposit(float amount, String description) throws NonPositiveAmountException;

    Transaction withdraw(float amount, String description) throws NonPositiveAmountException, NoMoneyException;

}
