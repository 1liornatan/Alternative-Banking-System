package bank.loans.investments.impl;

import bank.loans.interest.Interest;
import bank.loans.investments.Investment;

import java.util.ArrayList;
import java.util.List;

public class LoanInvestment implements Investment {
    private final String investorId;
    private final Interest interest;
    private int amountPaid, paymentsReceived;
    private List<Integer> payments;

    public LoanInvestment(String investorId, Interest interest) {
        this.investorId = investorId;
        this.interest = interest;

        amountPaid = 0;
        paymentsReceived = 0;

        payments = new ArrayList<>();
        setPayments();
    }

    @Override
    public int getPaymentsReceived() {
        return paymentsReceived;
    }
    private void setPayments() {

        int duration = interest.getDuration();
        int cycles = interest.getCyclesPerPayment();
        int paymentsCount = (duration / cycles);

        int totalPayment = interest.getFinalAmount();
        int basePayment = totalPayment / paymentsCount;

        int leftOver = totalPayment % paymentsCount;

        for(int i = 0; i < paymentsCount; i++) {
            int payment = basePayment;
            if(i < leftOver)
                payment++;

            payments.add(payment);

        }

    }

    @Override
    public int getAmountPaid() {
        return amountPaid;
    }

    @Override
    public void payment() {
        if(!isFullyPaid()) {
            amountPaid += getPayment();
            paymentsReceived++;
        }
    }


    @Override
    public int getBaseAmount() {
        return interest.getBaseAmount();
    }

    @Override
    public int getPayment() {
        if(isFullyPaid())
            return 0;

        return payments.get(paymentsReceived);
    }

    @Override
    public int getPayment(int index) {
        return payments.get(index);
    }

    @Override
    public int getRemainingPayment() {
        return getTotalPayment() - amountPaid;
    }

    @Override
    public int getTotalPayment() {
        return interest.getFinalAmount();
    }

    @Override
    public String getInvestorId() {
        return investorId;
    }

    @Override
    public boolean isFullyPaid() {
        return (amountPaid == getTotalPayment());
    }

}
