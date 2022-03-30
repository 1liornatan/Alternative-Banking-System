package bank.loans.handler;

import bank.accounts.Account;
import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.loans.Loan;
import bank.loans.investments.Investment;

public interface LoanHandler {
    Loan createLoan(int ownerId, float baseAmount, float interestPercent, String category);
    void addInvestment(Loan loan, Investment investment, Account srcAcc) throws NonPositiveAmountException, NoMoneyException;

}
