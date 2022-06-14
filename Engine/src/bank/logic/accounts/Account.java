package bank.logic.accounts;

import bank.logic.accounts.impl.exceptions.NoMoneyException;
import bank.logic.accounts.impl.exceptions.NonPositiveAmountException;
import bank.logic.data.Singular;
import bank.logic.transactions.Transaction;

import java.util.List;

/* An interface for all kind of accounts
   with the default actions required
 */
public interface Account extends Singular {

    List<Transaction> getTransactions();

    String getId();

    int getBalance();

    Transaction deposit(int amount, String description) throws NonPositiveAmountException;

    Transaction withdraw(int amount, String description) throws NonPositiveAmountException, NoMoneyException;


}
