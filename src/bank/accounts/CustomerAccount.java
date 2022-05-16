package bank.accounts;

import bank.loans.Loan;

public interface CustomerAccount extends Account {
    void addRequestedLoan(Loan loan);
    void addInvestedLoan(Loan loan);
    int getNumOfRelatedLoans();

}
