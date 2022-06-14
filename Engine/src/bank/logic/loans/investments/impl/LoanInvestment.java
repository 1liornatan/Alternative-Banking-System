package bank.logic.loans.investments.impl;

import bank.logic.loans.interest.Interest;
import bank.logic.loans.investments.Investment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoanInvestment implements Investment {
    private static int idGenerator = 40000;
    private String investorId;

    private final Interest interest;
    private int amountPaid;
    private int paymentsReceived;
    private final int id;
    private final List<Integer> payments;
    private final String loanId;

    public LoanInvestment(String investorId, Interest interest, String loanId) {
        this.investorId = investorId;
        this.interest = interest;
        this.id = idGenerator++;
        this.loanId = loanId;

        amountPaid = 0;
        paymentsReceived = 0;

        payments = new ArrayList<>();
        setPayments();
    }

    @Override
    public String getLoanId() {
        return loanId;
    }

    @Override
    public void setInvestorId(String investorId) {
        this.investorId = investorId;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public Interest getInterest() {
        return interest;
    }


    @Override
    public int getPaymentsReceived() {
        return paymentsReceived;
    }

    @Override
    public double getSellPrice() {
        int duration = interest.getDuration();
        int cycles = interest.getCyclesPerPayment();
        int paymentsCount = (duration / cycles);

        int paymentsLeft = paymentsCount - paymentsReceived;
        int baseAmount = getBaseAmount();
        return (paymentsLeft / paymentsCount) * baseAmount;
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
        if(index >= payments.size())
            return 0;

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

    @Override
    public String toString() {
        return "Investment in '" + loanId + "' for " + getSellPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanInvestment that = (LoanInvestment) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
