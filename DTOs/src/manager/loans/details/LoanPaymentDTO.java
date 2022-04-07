package manager.loans.details;

public class LoanPaymentDTO {
    int nextPaymentAmount, nextPaymentInYaz;
    int cyclesPerPayment;

    public int getNextPaymentAmount() {
        return nextPaymentAmount;
    }

    public int getNextPaymentInYaz() {
        return nextPaymentInYaz;
    }

    public int getCyclesPerPayment() {
        return cyclesPerPayment;
    }

    public LoanPaymentDTO(int nextPaymentAmount, int nextPaymentInYaz, int cyclesPerPayment) {
        this.nextPaymentAmount = nextPaymentAmount;
        this.nextPaymentInYaz = nextPaymentInYaz;
        this.cyclesPerPayment = cyclesPerPayment;
    }
}
