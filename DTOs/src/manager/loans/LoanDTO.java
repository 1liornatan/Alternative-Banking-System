package manager.loans;

import bank.loans.Loan;
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

    public LoanDTO(Loan loan, int currentYaz) {
        name = loan.getId();
        status = loan.getStatus();
        category = loan.getCategory();
        baseAmount = loan.getBaseAmount();
        finalAmount = loan.getFinalAmount();
        cyclesPerPayment = loan.getCyclesPerPayment();
        interestPercent = loan.getInterestPercent();
        loanAccountBalance = loan.getLoanAccount().getBalance();
        startedYaz = loan.getStartedYaz();
        endedYaz = loan.getFinishedYaz();
        nextPaymentYaz = cyclesPerPayment - ((currentYaz - startedYaz) % cyclesPerPayment);
        missingCycles = loan.getCurrentPayment() - loan.getFullPaidCycles();
        deriskAmount = loan.getDeriskAmount();
    }

}
