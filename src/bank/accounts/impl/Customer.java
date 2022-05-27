package bank.accounts.impl;

import bank.loans.Loan;
import bank.loans.LoanStatus;
import bank.messages.Notification;
import bank.transactions.Transaction;

import java.util.*;

public class Customer extends LoanAccount implements bank.accounts.CustomerAccount {
    private final String name;
    private List<Loan> loansRequested, loansInvested;
    private List<Notification> notificationList;

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

        loansRequested = new ArrayList<>();
        loansInvested = new ArrayList<>();
        notificationList = new ArrayList<>();
    }

    @Override
    public void addNotification(Notification notification) {
        notificationList.add(notification);
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
    }

    @Override
    public List<Loan> getLoansRequested() {
        return loansRequested;
    }

    @Override
    public List<Loan> getLoansInvested() {
        return loansInvested;
    }

    @Override
    public String getId() {
        return name;
    }


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
