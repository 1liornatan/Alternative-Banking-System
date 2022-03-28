package bank.loans.impl;

import bank.accounts.Account;
import bank.accounts.impl.LoanAccount;
import bank.loans.Loan;
import bank.loans.LoanStatus;
import bank.loans.interest.impl.BasicInterest;
import bank.loans.investments.Investment;

import java.util.Objects;
import java.util.Set;

public class BasicLoan implements Loan {
    private static int idGenerator = 40000;
    private int id, duration;
    private String category;
    private float baseAmount;
    private BasicInterest interest;
    private LoanStatus status;
    private Set<Investment> investments;
    private Account loanAccount;

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public int getDuration() {
        return duration;
    }

    public Set<Investment> getInvestments() {
        return investments;
    }

    public Account getLoanAccount() {
        return loanAccount;
    }

    public BasicLoan(float baseAmount, float interestPercent, String category) {
        this.category = category;
        this.baseAmount = baseAmount;
        this.interest = new BasicInterest(interestPercent, baseAmount);
        this.status = LoanStatus.PENDING;
        loanAccount = new LoanAccount();
        id = idGenerator++;
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getCategory() {
        return category;
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
        return baseAmount;
    }

    @Override
    public LoanStatus getStatus() {
        return status;
    }

    @Override
    public float getMaxPortion() {
        float invested = loanAccount.getBalance();
        float portion = (invested / baseAmount) * 100;

        return portion;
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
                ", duration=" + duration +
                ", category='" + category + '\'' +
                ", interest=" + interest +
                ", status=" + status +
                ", loanAccount=" + loanAccount +
                '}';
    }
}
