package bank.loans;

public enum LoanStatus {
    NEW("New"),
    PENDING("Pending"),
    ACTIVE("Active"),
    RISKED("Risked"),
    FINISHED("Finished");

    private final String statusText;

    LoanStatus(String statusText) {
        this.statusText = statusText;
    }

    @Override
    public String toString() {
        return statusText;
    }
}
