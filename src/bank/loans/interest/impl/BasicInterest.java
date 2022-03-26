package bank.loans.interest.impl;

import bank.loans.interest.Interest;

public class BasicInterest implements Interest {
    private float percent, baseAmount;

    public BasicInterest(float percent, float baseAmount) {
        this.percent = percent;
        this.baseAmount = baseAmount;
    }

    @Override
    public float getPercent() {
        return percent;
    }

    @Override
    public float getInterest() {
        return baseAmount * percent;
    }

    @Override
    public float getFinalAmount() {
        return baseAmount + getInterest();
    }

    public float getBaseAmount() {
        return baseAmount;
    }
}
