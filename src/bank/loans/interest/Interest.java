package bank.loans.interest;

public interface Interest {
    float getFinalAmount();
    float getInterest(); // returns calculated amount of interest to all payments

    int getCyclesPerPayment();

    int getDuration();

    float getPercent(); // returns interest's percent per payment
    float getBaseAmount();

}