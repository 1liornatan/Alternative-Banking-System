package bank.logic.loans.investments;

import bank.logic.data.Singular;
import bank.logic.loans.interest.Interest;

import java.io.Serializable;

public interface Investment extends Serializable, Singular {
    String getInvestorId();

    int getPayment(int index);

    int getRemainingPayment();

    int getBaseAmount();

    int getPayment();

    int getTotalPayment();

    String getLoanId();

    void setInvestorId(String investorId);

    Interest getInterest();

    int getPaymentsReceived();

    double getSellPrice();

    int getAmountPaid();

    void payment();

    boolean isFullyPaid();
}