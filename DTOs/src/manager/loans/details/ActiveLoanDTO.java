package manager.loans.details;

public class ActiveLoanDTO {
    int amountToActive, deriskAmount, missingCycles;

    public int getAmountToActive() {
        return amountToActive;
    }

    public int getDeriskAmount() {
        return deriskAmount;
    }

    public int getMissingCycles() {
        return missingCycles;
    }

    public ActiveLoanDTO(int amountToActive, int deriskAmount, int missingCycles) {
        this.amountToActive = amountToActive;
        this.deriskAmount = deriskAmount;
        this.missingCycles = missingCycles;
    }
}
