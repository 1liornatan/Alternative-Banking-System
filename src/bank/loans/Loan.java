package bank.loans;

import bank.accounts.Account;
import bank.data.Singular;
import bank.loans.investments.Investment;

import java.util.Set;

public interface Loan extends Singular {

    int getId();
    String getCategory();
    float getFinalAmount();
    float getInterest();
    float getBaseAmount();
    LoanStatus getStatus();
    float getMaxPortion();

    Set<Investment> getInvestments();

    void addInvestment(Investment investment);
    public Account getLoanAccount();
    float getCyclePayment();
    int getOwnerId();
}