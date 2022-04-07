package bank.accounts;

import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.data.Singular;
import bank.loans.Loan;
import bank.transactions.Transaction;

import java.util.List;

/* An interface for all kind of accounts
   with the default actions required
 */
public interface Account extends Singular {

    List<Transaction> getTransactions();

    void addRequestedLoan(Loan loan);

    void addInvestedLoan(Loan loan);

    List<Loan> getLoansRequested();

    List<Loan> getLoansInvested();

    String getId();

    int getBalance();

    Transaction deposit(int amount, String description) throws NonPositiveAmountException;

    Transaction withdraw(int amount, String description) throws NonPositiveAmountException, NoMoneyException;

}
