package manager.loans;

import manager.loans.details.*;

public class LoanDTO {
    LoanDetailsDTO details;
    InterestDTO interest;
    YazDTO yazDetails;
    LoanPaymentDTO paymentDetails;
    ActiveLoanDTO activeLoanDTO;

    public LoanDTO(LoanDetailsDTO details, InterestDTO interest, YazDTO yazDetails,
                   LoanPaymentDTO paymentDetails, ActiveLoanDTO activeLoanDTO) {
        this.details = details;
        this.interest = interest;
        this.yazDetails = yazDetails;
        this.paymentDetails = paymentDetails;
        this.activeLoanDTO = activeLoanDTO;
    }

    public LoanDetailsDTO getDetails() {
        return details;
    }

    public InterestDTO getInterest() {
        return interest;
    }

    public YazDTO getYazDetails() {
        return yazDetails;
    }

    public LoanPaymentDTO getPaymentDetails() {
        return paymentDetails;
    }

    public ActiveLoanDTO getActiveLoanDTO() {
        return activeLoanDTO;
    }
}
