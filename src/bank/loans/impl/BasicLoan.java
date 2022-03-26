package bank.loans.impl;

import bank.loans.Loan;
import bank.loans.LoanStatus;
import bank.loans.interest.impl.BasicInterest;

public class BasicLoan implements Loan {
    private static int idGenerator = 40000;
    private int id;
    private String category;
    private float baseAmount;
    private BasicInterest interest;
    private LoanStatus status;

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public BasicLoan(String category, float baseAmount, float interestPercent) {
        this.category = category;
        this.baseAmount = baseAmount;
        this.interest = new BasicInterest(interestPercent, baseAmount);
        this.status = LoanStatus.PENDING;
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

}
