package manager.loans;

public class LoansWithVersion {
    private final LoansData data;
    private final int customersVer;
    private final int loansVer;

    public LoansWithVersion(LoansData data, int customersVer, int loansVer) {
        this.data = data;
        this.customersVer = customersVer;
        this.loansVer = loansVer;
    }

    public LoansData getData() {
        return data;
    }

    public int getCustomersVer() {
        return customersVer;
    }

    public int getLoansVer() {
        return loansVer;
    }
}
