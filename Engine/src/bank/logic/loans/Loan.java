package bank.logic.loans;

import bank.logic.accounts.Account;
import bank.logic.data.Singular;
import bank.logic.loans.investments.Investment;

import java.util.List;

public interface Loan extends Singular {

    int getMissingCycles();

    int getAmountToActive();

    int getNextYaz();

    int getCurrentPayment();

    int getFinishedYaz();

    void setFinishedYaz(int finishedYaz);

    void setPayments();

    String getId();
    String getCategory();
    int getFinalAmount();
    int getInterestAmount();
    int getInterestPercent();

    int getCyclesPerPayment();

    int getBaseAmount();

    int getPayment();

    LoanStatus getStatus();
    void setStatus(LoanStatus status);
    float getMaxPortion();

    int getDuration();

    List<Investment> getInvestments();
    void addInvestment(Investment investment);

    int getStartedYaz();

    void setStartedYaz(int startedYaz);

    Account getLoanAccount();

    void fullPaymentCycle();

    int getFullPaidCycles();

    void nextPayment();

    String getOwnerId();

    int getDeriskAmount();

    boolean isInvestable();

    int getNextPayment();

    int getAmountToCloseLoan();
}