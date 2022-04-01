package bank.loans.impl;

import bank.accounts.Account;
import bank.accounts.impl.LoanAccount;
import bank.loans.Loan;
import bank.loans.LoanStatus;
import bank.loans.impl.builder.LoanBuilder;
import bank.loans.interest.Interest;
import bank.loans.interest.impl.BasicInterest;
import bank.loans.investments.Investment;

import java.util.Objects;
import java.util.Set;

public class BasicLoan implements Loan {

    private static int idGenerator = 40000;
    private final int id;
    private final LoanBuilder loanDetails;
    private int duration;
    private final Interest interest;
    private LoanStatus status;
    private Set<Investment> investments;
    private final Account loanAccount;


    @Override
    public int getOwnerId() {
        return loanDetails.getOwnerId();
    }

    @Override
    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public Set<Investment> getInvestments() {
        return investments;
    }

    @Override
    public void addInvestment(Investment investment) { investments.add(investment); }

    @Override
    public Account getLoanAccount() {
        return loanAccount;
    }

    public BasicLoan(LoanBuilder loanDetails, Interest interest) {
        this.loanDetails = loanDetails;
        this.interest = interest;
        this.status = LoanStatus.PENDING;
        loanAccount = new LoanAccount();
        id = idGenerator++;
    }

    @Override
    public String getIdName() {
        return loanDetails.getIdName();
    }
    @Override
    public int getId() {
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
        return getFinalAmount() / duration;
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
        return id == basicLoan.id;
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
                ", duration=" + duration +
                ", interest=" + interest +
                ", status=" + status +
                ", investments=" + investments +
                ", loanAccount=" + loanAccount +
                '}';
    }
}
