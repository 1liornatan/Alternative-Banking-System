package manager.loans;

public class LoanData {
    String name, category, status, loanRequester;
    int baseAmount, finalAmount;
    float interest;
    int startedYaz, finishedYaz;
    int nextPaymentInYaz, nextPaymentAmount, cyclesPerPayment;
    int amountToActive, deriskAmount, missingCycles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(int baseAmount) {
        this.baseAmount = baseAmount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(int finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getLoanRequester() {
        return loanRequester;
    }

    public void setLoanRequester(String loanRequester) {
        loanRequester = loanRequester;
    }

    public float getInterest() {
        return interest;
    }

    public void setInterest(float interest) {
        this.interest = interest;
    }

    public int getStartedYaz() {
        return startedYaz;
    }

    public void setStartedYaz(int startedYaz) {
        this.startedYaz = startedYaz;
    }

    public int getFinishedYaz() {
        return finishedYaz;
    }

    public void setFinishedYaz(int finishedYaz) {
        this.finishedYaz = finishedYaz;
    }

    public int getNextPaymentInYaz() {
        return nextPaymentInYaz;
    }

    public void setNextPaymentInYaz(int nextPaymentInYaz) {
        this.nextPaymentInYaz = nextPaymentInYaz;
    }

    public int getNextPaymentAmount() {
        return nextPaymentAmount;
    }

    public void setNextPaymentAmount(int nextPaymentAmount) {
        this.nextPaymentAmount = nextPaymentAmount;
    }

    public int getCyclesPerPayment() {
        return cyclesPerPayment;
    }

    public void setCyclesPerPayment(int cyclesPerPayment) {
        this.cyclesPerPayment = cyclesPerPayment;
    }

    public int getAmountToActive() {
        return amountToActive;
    }

    public void setAmountToActive(int amountToActive) {
        this.amountToActive = amountToActive;
    }

    public int getDeriskAmount() {
        return deriskAmount;
    }

    public void setDeriskAmount(int deriskAmount) {
        this.deriskAmount = deriskAmount;
    }

    public int getMissingCycles() {
        return missingCycles;
    }

    public void setMissingCycles(int missingCycles) {
        this.missingCycles = missingCycles;
    }
}
