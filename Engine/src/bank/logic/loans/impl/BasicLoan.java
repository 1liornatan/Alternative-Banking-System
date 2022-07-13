package bank.logic.loans.impl;

import bank.logic.accounts.Account;
import bank.logic.accounts.impl.LoanAccount;
import bank.logic.loans.Loan;
import bank.logic.loans.LoanStatus;
import bank.logic.loans.impl.builder.LoanBuilder;
import bank.logic.loans.interest.Interest;
import bank.logic.loans.investments.Investment;
import bank.logic.time.TimeHandler;

import java.util.*;

public class BasicLoan implements Loan {

    private final String id;
    private final LoanBuilder loanDetails;
    private int startedYaz;
    private int finishedYaz;
    private final int averagePayment;
    private final Interest interest;
    private LoanStatus status;
    private final List<Investment> investments;
    private final List<Integer> payments;
    private final Account loanAccount;
    private final TimeHandler timeHandler;
    private final List<Integer> cyclesPaid;
    private boolean isClosed;
    private int closedYaz;

    @Override
    public void fullPaymentCycle() {
        cyclesPaid.add(timeHandler.getCurrentTime());
    }

    @Override
    public int getFullPaidCycles() {
        int currYaz = timeHandler.getCurrentTime();
        return Math.toIntExact(cyclesPaid.stream().filter(time -> time <= currYaz).count());
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
        this.timeHandler = timeHandler;

        closedYaz = -1;
        isClosed = false;
        loanAccount = new LoanAccount(0);
        id = loanDetails.getIdName();

        investments = new ArrayList<>();
        startedYaz = 0;
        finishedYaz = 0;


        averagePayment = interest.getFinalAmount() / (interest.getDuration() / interest.getCyclesPerPayment());
        payments = new ArrayList<>();
        cyclesPaid = new ArrayList<>();
    }

    @Override
    public int getMissingCycles() {
        int currYaz = timeHandler.getCurrentTime();
        int cycles = Math.toIntExact(cyclesPaid.stream().filter(time -> time <= currYaz).count());
        return getPaymentNeeded() - cycles;
    }


    @Override
    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public int getClosedYaz() {
        return closedYaz;
    }

    @Override
    public void closeLoan() {
        isClosed = true;
        closedYaz = timeHandler.getCurrentTime();
        status = LoanStatus.FINISHED;
        finishedYaz = closedYaz;
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
        int currYaz = timeHandler.getCurrentTime();
        int size = Math.toIntExact(cyclesPaid.stream().filter(time -> time <= currYaz).count());
        return size;
    }

    @Override
    public int getNextPayment() {
        int currPayment = getPaymentNeeded();
        if(currPayment >= payments.size())
            return 0;

        return payments.get(currPayment);
    }

    @Override
    public int getAmountToCloseLoan() {
        if(payments.size() <= 0)
            return getBaseAmount();

        int currentPayment = getCurrentPayment();
        int paymentNeeded = getPaymentNeeded();

        if(paymentNeeded < currentPayment) {
            paymentNeeded = currentPayment;
        }
        else if(paymentNeeded > payments.size()) {
            paymentNeeded = payments.size();
        }

        int sum = payments.subList(currentPayment, paymentNeeded).stream().mapToInt(Integer::intValue).sum();
        int left = payments.size() - paymentNeeded;
        sum += left * (interest.getBaseAmount() / (getDuration() / getCyclesPerPayment()));
        return sum;
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
    public int getInterestPercent() {
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
        if(status == LoanStatus.ACTIVE || status == LoanStatus.RISKED)
            return payments.get(cyclesPaid.size());

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
        int cycles = ((timeHandler.getCurrentTime() - startingYaz) / getCyclesPerPayment()) - 1;
        int sum = 0;

        if(cycles > 0) {
            for (Investment investment : investments) {
                for (int i = investment.getPaymentsReceived(); i < cycles; i++) {
                    sum += investment.getPayment(i);
                }
            }
        }
        return sum;
    }

    @Override
    public boolean isInvestable() {
        return (status == LoanStatus.NEW || status == LoanStatus.PENDING);
    }

    private int getPaymentNeeded() {
        int duration = timeHandler.getCurrentTime() - getStartedYaz();
        return duration / getCyclesPerPayment();
    }
}
