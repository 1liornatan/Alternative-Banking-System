package bank.loans.handler;

import bank.accounts.CustomerAccount;
import bank.accounts.impl.Customer;
import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.impl.exceptions.DataNotFoundException;
import bank.loans.Loan;
import bank.loans.impl.builder.LoanBuilder;
import bank.loans.interest.Interest;
import bank.loans.investments.Investment;

public interface LoanHandler {

    Loan createLoan(LoanBuilder loadDetails, Interest interest);

    void addInvestment(Loan loan, Investment investment, CustomerAccount srcAcc) throws NonPositiveAmountException, NoMoneyException, DataNotFoundException;

}
