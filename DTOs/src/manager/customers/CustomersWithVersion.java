package manager.customers;

public class CustomersWithVersion {
    private final CustomersData data;
    private final int customersVer;
    private final int loansVer;

    public CustomersWithVersion(CustomersData data, int customersVer, int loansVer) {
        this.data = data;
        this.customersVer = customersVer;
        this.loansVer = loansVer;
    }

    public CustomersData getData() {
        return data;
    }

    public int getCustomersVer() {
        return customersVer;
    }

    public int getLoansVer() {
        return loansVer;
    }
}
