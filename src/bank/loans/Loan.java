package bank.loans;

import bank.accounts.Account;
import bank.data.Singular;
import bank.loans.investments.Investment;

import java.util.List;

public interface Loan extends Singular {

    int getCurrentPayment();

    int getFinishedYaz();

    void setFinishedYaz(int finishedYaz);

    void setPayments();

    String getId();
    String getCategory();
    int getFinalAmount();
    int getInterestAmount();
    float getInterestPercent();

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
}