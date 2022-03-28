package bank.loans.investments;

public interface Investment {
    int getInvestorId();

    float getRemainingPayment();

    float getBaseAmount();

    float getPayment();

    float getTotalPayment();

    void payment();
}