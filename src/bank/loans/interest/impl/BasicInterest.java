package bank.loans.interest.impl;

import bank.loans.interest.Interest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BasicInterest implements Interest {
    private float percent;
    private final int cyclesPerPayment, duration, baseAmount;

    public BasicInterest(float percent, int baseAmount, int cyclesPerPayment, int duration) {
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
    public int getInterest() {
        return (int) (baseAmount * percent / 100 );
    }

    @Override
    public int getFinalAmount() {
        return baseAmount + getInterest();
    }

    @Override
    public int getBaseAmount() {
        return baseAmount;
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
