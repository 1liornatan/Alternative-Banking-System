package bank.loans.investments;

import java.io.Serializable;

public interface Investment extends Serializable {
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