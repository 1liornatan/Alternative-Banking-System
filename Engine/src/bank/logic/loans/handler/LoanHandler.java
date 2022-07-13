package bank.logic.loans.handler;

import bank.logic.accounts.CustomerAccount;
import bank.logic.accounts.impl.exceptions.NoMoneyException;
import bank.logic.accounts.impl.exceptions.NonPositiveAmountException;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.loans.Loan;
import bank.logic.loans.impl.builder.LoanBuilder;
import bank.logic.loans.interest.Interest;
import bank.logic.loans.investments.Investment;

public interface LoanHandler {

    Loan createLoan(LoanBuilder loadDetails, Interest interest);

    void addInvestment(Loan loan, Investment investment, CustomerAccount srcAcc) throws NonPositiveAmountException, NoMoneyException, DataNotFoundException;

    void payLoanByAmount(Loan loanId, int amount) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException;

    void closeLoan(Loan loan) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException;

    void calculateLoansStatus();
}
