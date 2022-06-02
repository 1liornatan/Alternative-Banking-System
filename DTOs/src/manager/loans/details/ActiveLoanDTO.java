package manager.loans.details;

public class ActiveLoanDTO {
    final int amountToActive;
    final int deriskAmount;
    final int missingCycles;

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
