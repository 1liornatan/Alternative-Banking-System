package bank.accounts;

import bank.accounts.impl.Customer;
import bank.loans.Loan;
import bank.loans.LoanStatus;
import bank.messages.Notification;
import manager.customers.CustomerData;

import java.util.Collection;
import java.util.List;

public interface CustomerAccount extends Account {
    void addNotification(Notification notification);

    List<Notification> getNotificationList();

    void addRequestedLoan(Loan loan);
    void addInvestedLoan(Loan loan);
    int getNumOfInvestedLoansByStatus(LoanStatus status);

    Collection<Loan> getLoansRequested();

    Collection<Loan> getLoansInvested();

    int getNumOfRequestedLoansByStatus(LoanStatus status);
}
