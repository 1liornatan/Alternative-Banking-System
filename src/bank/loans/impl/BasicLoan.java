package bank.loans.impl;

import bank.accounts.Account;
import bank.accounts.impl.LoanAccount;
import bank.loans.Loan;
import bank.loans.LoanStatus;
import bank.loans.impl.builder.LoanBuilder;
import bank.loans.interest.Interest;
import bank.loans.investments.Investment;
import bank.time.TimeHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BasicLoan implements Loan {

    private final String id;
    private final LoanBuilder loanDetails;
    private int startedYaz;
    private int finishedYaz;
    private int currentPayment;
    private int fullPaidCycles;
    private final int averagePayment;
    private final Interest interest;
    private LoanStatus status;
    private final List<Investment> investments;
    private final List<Integer> payments;
    private final Account loanAccount;
    private final TimeHandler timeHandler;

    @Override
    public void fullPaymentCycle() {
        fullPaidCycles++;
    }

    @Override
    public int getFullPaidCycles() {
        return fullPaidCycles;
    }

    @Override
    public void nextPayment() {
        currentPayment++;
    }

    @Override
    public String getOwnerId() {
        return loanDetails.getOwnerId();
    }

    @Override
    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    @Override
    public int getDuration() {
        return interest.getDuration();
    }

    @Override
    public List<Investment> getInvestments() {
        return investments;
    }

    @Override
    public void addInvestment(Investment investment) {
        investments.add(investment);
    }

    @Override
    public int getStartedYaz() {
        return startedYaz;
    }

    @Override
    public void setStartedYaz(int startedYaz) {
        this.startedYaz = startedYaz;
    }

    @Override
    public Account getLoanAccount() {
        return loanAccount;
    }

    public BasicLoan(LoanBuilder loanDetails, Interest interest, TimeHandler timeHandler) {
        this.loanDetails = loanDetails;
        this.interest = interest;
        this.status = LoanStatus.NEW;
        loanAccount = new LoanAccount();
        id = loanDetails.getIdName();

        investments = new ArrayList<>();
        startedYaz = 0;
        finishedYaz = 0;

        currentPayment = 0;
        fullPaidCycles = 0;

        averagePayment = interest.getFinalAmount() / (interest.getDuration() / interest.getCyclesPerPayment());
        payments = new ArrayList<>();
        this.timeHandler = timeHandler;
    }

    @Override
    public int getMissingCycles() {
        return currentPayment - fullPaidCycles;
    }

    @Override
    public int getAmountToActive() {
        return (getBaseAmount() - loanAccount.getBalance());
    }

    @Override
    public int getNextYaz() {
        int currentYaz = timeHandler.getCurrentTime();
        int cyclesPerPayment = getCyclesPerPayment();
        return cyclesPerPayment - ((currentYaz - startedYaz) % cyclesPerPayment);
    }

    @Override
    public int getCurrentPayment() {
        return currentPayment;
    }

    @Override
    public int getFinishedYaz() {
        return finishedYaz;
    }

    @Override
    public void setFinishedYaz(int finishedYaz) {
        this.finishedYaz = finishedYaz;
    }

    @Override
    public void setPayments() {
        int sum;
        int duration = interest.getDuration() / interest.getCyclesPerPayment();

        for (int i = 0; i < duration; i++) {
            sum = 0;
            for (Investment investment : investments) {
                sum += investment.getPayment(i);
            }
            payments.add(sum);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCategory() {
        return loanDetails.getCategory();
    }

    @Override
    public int getFinalAmount() {
        return interest.getFinalAmount();
    }

    @Override
    public int getInterestAmount() {
        return interest.getInterest();
    }

    @Override
    public float getInterestPercent() {
        return interest.getPercent();
    }

    @Override
    public int getCyclesPerPayment() {
        return interest.getCyclesPerPayment();
    }

    @Override
    public int getBaseAmount() {
        return interest.getBaseAmount();
    }

    @Override
    public int getPayment() {
        if(status == LoanStatus.ACTIVE || status == LoanStatus.RISK)
            return payments.get(currentPayment);

        return averagePayment;
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
        return Objects.equals(id, basicLoan.id);
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
                ", interest=" + interest +
                ", status=" + status +
                ", investments=" + investments +
                ", loanAccount=" + loanAccount +
                '}';
    }

    @Override
    public int getDeriskAmount() {
        int startingYaz = startedYaz;
        int cycles = (timeHandler.getCurrentTime() - startingYaz) / getCyclesPerPayment();
        int sum = 0;

        for (Investment investment : investments) {
            for (int i = investment.getPaymentsReceived(); i < cycles; i++) {
                sum += investment.getPayment(i);
            }
        }
        return sum;
    }

    @Override
    public boolean isInvestable() {
        return (status == LoanStatus.NEW || status == LoanStatus.PENDING);
    }
}
