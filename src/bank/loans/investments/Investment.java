package bank.loans.investments;

import bank.data.Singular;
import bank.loans.interest.Interest;

import java.io.Serializable;

public interface Investment extends Serializable, Singular {
    String getInvestorId();

    int getPayment(int index);

    int getRemainingPayment();

    int getBaseAmount();

    int getPayment();

    int getTotalPayment();

    void setInvestorId(String investorId);

    Interest getInterest();

    int getPaymentsReceived();

    int getAmountPaid();

    void payment();

    boolean isFullyPaid();
}