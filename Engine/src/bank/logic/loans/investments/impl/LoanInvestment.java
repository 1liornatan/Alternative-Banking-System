package bank.logic.loans.investments.impl;

import bank.logic.loans.interest.Interest;
import bank.logic.loans.investments.Investment;
import bank.logic.time.TimeHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoanInvestment implements Investment {
    private static int idGenerator = 40000;
    private String investorId;

    private final Interest interest;
    private final int id;
    private final List<Integer> payments;
    private final List<Integer> cyclesPaid;
    private final String loanId;
    private final TimeHandler timeHandler;

    public LoanInvestment(String investorId, Interest interest, String loanId, TimeHandler timeHandler) {
        this.investorId = investorId;
        this.interest = interest;
        this.id = idGenerator++;
        this.loanId = loanId;
        this.timeHandler = timeHandler;

        payments = new ArrayList<>();
        setPayments();
        cyclesPaid = new ArrayList<>();
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
        int currYaz = timeHandler.getCurrentTime();
        return Math.toIntExact(cyclesPaid.stream().filter(time -> time <= currYaz).count());
    }

    @Override
    public double getSellPrice() {
        int duration = interest.getDuration();
        int cycles = interest.getCyclesPerPayment();
        int paymentsCount = (duration / cycles);

        int paymentsLeft = paymentsCount - getPaymentsReceived();
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
        int currYaz = timeHandler.getCurrentTime();
        int size = Math.toIntExact(cyclesPaid.stream().filter(time -> time <= currYaz).count());

        return payments.subList(0, size).stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public void payment() {
        if(!isFullyPaid()) {
            cyclesPaid.add(timeHandler.getCurrentTime());
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

        int currYaz = timeHandler.getCurrentTime();

        return payments.get(Math.toIntExact(cyclesPaid.stream().filter(time -> time <= currYaz).count()));
    }

    @Override
    public int getPayment(int index) {
        if(index >= payments.size())
            return 0;

        return payments.get(index);
    }

    @Override
    public int getRemainingPayment() {
        return getTotalPayment() - getAmountPaid();
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
        return (getAmountPaid() == getTotalPayment());
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
