package bank.loans.investments;

public interface Investment {
    String getInvestorId();

    float getRemainingPayment();

    float getBaseAmount();

    float getPayment();

    float getTotalPayment();

    float getAmountPaid();

    void payment();

    boolean isFullyPaid();
}