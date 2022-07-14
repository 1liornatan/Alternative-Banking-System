package bank.logic.accounts.impl;

import bank.logic.accounts.CustomerAccount;
import bank.logic.loans.Loan;
import bank.logic.loans.LoanStatus;
import bank.logic.messages.Notification;

import java.util.*;

public class Customer extends LoanAccount implements CustomerAccount {
    private final String name;
    private final Collection<Loan> loansRequested;
    private final Collection<Loan> loansInvested;
    private final List<Notification> notificationList;
    private int notificationsVersion;
    private int investmentsVersion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer that = (Customer) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Customer(String name, int balance) {
        super(balance);
        this.name = name;

        loansRequested = new HashSet<>();
        loansInvested = new HashSet<>();
        notificationList = new ArrayList<>();
        notificationsVersion = 1;
        investmentsVersion = 1;
    }

    @Override
    public void addNotification(Notification notification) {
        notificationList.add(notification);
        updateNotificationsVersion();
    }

    @Override
    public List<Notification> getNotificationList() {
        return notificationList;
    }

    @Override
    public void addRequestedLoan(Loan loan) {
        loansRequested.add(loan);
    }
    @Override
    public void addInvestedLoan(Loan loan) {
        loansInvested.add(loan);
        updateInvestmentsVersion();
    }

    @Override
    public Collection<Loan> getLoansRequested() {
        return loansRequested;
    }

    @Override
    public Collection<Loan> getLoansInvested() {
        return loansInvested;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public int getNotificationsVersion() {
        return notificationsVersion;
    }
    @Override
    public void updateNotificationsVersion() {notificationsVersion++;}
    @Override
    public int getInvestmentsVersion() {
        return investmentsVersion;
    }
    @Override
    public void updateInvestmentsVersion() {investmentsVersion++;}

    @Override
    public String toString() {
        return "Account Name: " + name +
                "'" + super.toString();
    }

    public int getNumOfRequestedLoansByStatus(LoanStatus status) {
        int counter = 0;
        for(Loan loan : loansRequested) {
            if(loan.getStatus() == status)
                counter++;
        }
        return counter;
    }

    public int getNumOfInvestedLoansByStatus(LoanStatus status) {
        int counter = 0;
        for(Loan loan : loansInvested) {
            if(loan.getStatus() == status)
                counter++;
        }
        return counter;
    }

}
