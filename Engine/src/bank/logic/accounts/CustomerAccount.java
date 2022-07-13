package bank.logic.accounts;

import bank.logic.loans.Loan;
import bank.logic.loans.LoanStatus;
import bank.logic.messages.Notification;

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

    void updateNotificationsVersion();
    int getNotificationsVersion();


    int getNumOfRequestedLoansByStatus(LoanStatus status);
}
