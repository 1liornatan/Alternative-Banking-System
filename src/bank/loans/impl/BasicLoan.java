package bank.loans.impl;

import bank.accounts.Account;
import bank.accounts.impl.LoanAccount;
import bank.loans.Loan;
import bank.loans.LoanStatus;
import bank.loans.impl.builder.LoanBuilder;
import bank.loans.interest.Interest;
import bank.loans.investments.Investment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BasicLoan implements Loan {

    private final String id;
    private final LoanBuilder loanDetails;
    private int startingYaz;
    private final Interest interest;
    private LoanStatus status;
    private final List<Investment> investments;
    private final Account loanAccount;


    @Override
    public String getOwnerId() {
        return loanDetails.getOwnerId();
    }

    @Override
    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    @Override
    public int getDuration() {
        return interest.getDuration();
    }

    @Override
    public List<Investment> getInvestments() {
        return investments;
    }

    @Override
    public void addInvestment(Investment investment) { investments.add(investment); }

    @Override
    public int getStartingYaz() {
        return startingYaz;
    }

    @Override
    public void setStartingYaz(int startingYaz) {
        this.startingYaz = startingYaz;
    }

    @Override
    public Account getLoanAccount() {
        return loanAccount;
    }

    public BasicLoan(LoanBuilder loanDetails, Interest interest) {
        this.loanDetails = loanDetails;
        this.interest = interest;
        this.status = LoanStatus.PENDING;
        loanAccount = new LoanAccount();
        id = loanDetails.getIdName();

        investments = new ArrayList<>();
        startingYaz = -1;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCategory() {
        return loanDetails.getCategory();
    }
    @Override
    public float getFinalAmount() {
        return interest.getFinalAmount();
    }

    @Override
    public float getInterest() {
        return interest.getInterest();
    }

    @Override
    public float getBaseAmount() {
        return interest.getBaseAmount();
    }

    @Override
    public float getCyclePayment() {
        return getFinalAmount() / getDuration();
    }

    @Override
    public LoanStatus getStatus() {
        return status;
    }

    @Override
    public float getMaxPortion() {
        float invested = loanAccount.getBalance();

        return (invested / getBaseAmount()) * 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicLoan basicLoan = (BasicLoan) o;
        return Objects.equals(id, basicLoan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BasicLoan{" +
                "id=" + id +
                ", loanDetails=" + loanDetails +
                ", interest=" + interest +
                ", status=" + status +
                ", investments=" + investments +
                ", loanAccount=" + loanAccount +
                '}';
    }
}
