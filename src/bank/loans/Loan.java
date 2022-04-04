package bank.loans;

import bank.accounts.Account;
import bank.data.Singular;
import bank.loans.investments.Investment;

import java.util.List;
import java.util.Set;

public interface Loan extends Singular {

    int getId();
    String getIdName();
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
    int getOwnerId();
}