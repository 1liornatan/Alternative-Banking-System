package bank.loans.investments;

public interface Investment {
    String getInvestorId();

    int getPayment(int index);

    int getRemainingPayment();

    int getBaseAmount();

    int getPayment();

    int getTotalPayment();

    int getPaymentsReceived();

    int getAmountPaid();

    void payment();

    boolean isFullyPaid();
}