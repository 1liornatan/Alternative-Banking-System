package bank.accounts;

import bank.accounts.impl.Customer;
import bank.loans.Loan;
import bank.loans.LoanStatus;
import manager.customers.CustomerData;

import java.util.List;

public interface CustomerAccount extends Account {
    void addRequestedLoan(Loan loan);
    void addInvestedLoan(Loan loan);
    int getNumOfInvestedLoansByStatus(LoanStatus status);
    int getNumOfRequestedLoansByStatus(LoanStatus status);
}