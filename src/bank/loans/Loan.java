package bank.loans;

import bank.accounts.Account;
import bank.data.Singular;
import bank.loans.investments.Investment;

import java.util.List;

public interface Loan extends Singular {

    void setPayments();

    String getId();
    String getCategory();
    int getFinalAmount();
    int getInterest();

    int getCyclesPerPayment();

    int getBaseAmount();

    int getPayment();

    LoanStatus getStatus();
    void setStatus(LoanStatus status);
    float getMaxPortion();

    int getDuration();

    List<Investment> getInvestments();
    void addInvestment(Investment investment);

    int getStartingYaz();

    void setStartingYaz(int startingYaz);

    Account getLoanAccount();
    String getOwnerId();
}