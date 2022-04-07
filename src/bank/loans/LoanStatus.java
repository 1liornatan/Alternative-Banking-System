package bank.loans;

public enum LoanStatus {
    NEW("New"),
    PENDING("Pending"),
    ACTIVE("Active"),
    RISK("In Risk"),
    FINISHED("Finished");

    private String statusText;

    LoanStatus(String statusText) {
        this.statusText = statusText;
    }

    @Override
    public String toString() {
        return statusText;
    }
}
