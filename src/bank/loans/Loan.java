package bank.loans;

import bank.data.Singular;

public interface Loan extends Singular {

    int getId();
    String getCategory();
    float getFinalAmount();
    float getInterest();
    float getBaseAmount();
    LoanStatus getStatus();

}