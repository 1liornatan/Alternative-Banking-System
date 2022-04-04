package bank.loans;

import bank.accounts.Account;
import bank.data.Singular;
import bank.loans.investments.Investment;

import java.util.List;

public interface Loan extends Singular {

    String getId();
    String getCategory();
    float getFinalAmount();
    float getInterest();
    float getBaseAmount();
    LoanStatus getStatus();
    void setStatus(LoanStatus status);
    float getMaxPortion();

    int getDuration();

    List<Investment> getInvestments();
    void addInvestment(Investment investment);

    int getStartingYaz();

    void setStartingYaz(int startingYaz);

    Account getLoanAccount();
    float getCyclePayment();
    String getOwnerId();
}