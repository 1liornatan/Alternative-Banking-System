package bank.loans.interest.impl;

import bank.loans.interest.Interest;

import java.util.Objects;

public class BasicInterest implements Interest {
    private float percent, baseAmount;
    private final int cyclesPerPayment, duration;

    public BasicInterest(float percent, float baseAmount, int cyclesPerPayment, int duration) {
        this.percent = percent;
        this.baseAmount = baseAmount;
        this.cyclesPerPayment = cyclesPerPayment;
        this.duration = duration;
    }

    @Override
    public int getCyclesPerPayment() {
        return cyclesPerPayment;
    }

    @Override
    public int getDuration() {
        return duration;
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

    @Override
    public float getBaseAmount() {
        return baseAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicInterest that = (BasicInterest) o;
        return Float.compare(that.percent, percent) == 0 && Float.compare(that.baseAmount, baseAmount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(percent, baseAmount);
    }

    @Override
    public String toString() {
        return "BasicInterest{" +
                "percent=" + percent +
                ", baseAmount=" + baseAmount +
                ", totalPayment=" + getFinalAmount() +
                '}';
    }
}
