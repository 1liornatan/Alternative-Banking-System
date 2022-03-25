package bank.loans;

public interface Loan {

    int getId();
    String getCategory();
    float getFinalAmount();
    float getInterest();
    float getBaseAmount();
    LoanStatus getStatus();
    String getRequesterName();
    int getRequesterId();

}