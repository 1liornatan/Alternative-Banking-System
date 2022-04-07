package manager.loans;

import bank.loans.LoanStatus;

public class LoanDTO {
    String name;
    LoanStatus status;
    String category;
    int baseAmount;
    int finalAmount;
    int cyclesPerPayment;
    float interestPercent;
    int loanAccountBalance;
    int startedYaz;
    int endedYaz;
    int nextPaymentYaz;
    int nextPayment;
    int missingCycles;
    int deriskAmount;

}
