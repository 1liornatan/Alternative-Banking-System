package bank.loans.investments.impl;

import bank.loans.interest.Interest;
import bank.loans.investments.Investment;

public class LoanInvestment implements Investment {
    private int investorId, duration;
    private Interest interest;
    private float amountPaid;

    public LoanInvestment(int investorId, Interest interest, int duration) {
        this.investorId = investorId;
        this.interest = interest;
        this.duration = duration;
        amountPaid = 0;
    }

    public void payment() {
        amountPaid += getPayment();
    }


    @Override
    public float getBaseAmount() {
        return interest.getBaseAmount();
    }

    @Override
    public float getPayment() {
        return getBaseAmount() / duration;
    }

    @Override
    public float getRemainingPayment() {
        return getTotalPayment() - amountPaid;
    }

    @Override
    public float getTotalPayment() {
        return interest.getFinalAmount();
    }

    @Override
    public int getInvestorId() {
        return investorId;
    }

}
