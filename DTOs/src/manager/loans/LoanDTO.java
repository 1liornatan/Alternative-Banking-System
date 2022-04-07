package manager.loans;

import bank.loans.Loan;
import bank.loans.LoanStatus;
import manager.loans.details.InterestDTO;
import manager.loans.details.LoanDetailsDTO;
import manager.loans.details.YazDTO;

public class LoanDTO {
    LoanDetailsDTO details;
    InterestDTO interest;
    YazDTO yazDetails;
    int cyclesPerPayment;
    int loanAccountBalance;
    int startedYaz;
    int endedYaz;
    int nextPaymentYaz;
    int nextPayment;
    int missingCycles;
    int deriskAmount;

    public LoanDTO(LoanDetailsDTO loanDetails, InterestDTO interest, YazDTO yazDetails) {
        this.details = loanDetails;
        this.interest = interest;
        this.yazDetails = yazDetails;
        cyclesPerPayment = loan.getCyclesPerPayment();
        loanAccountBalance = loan.getLoanAccount().getBalance();
        missingCycles = loan.getCurrentPayment() - loan.getFullPaidCycles();
        deriskAmount = loan.getDeriskAmount();
    }

}
