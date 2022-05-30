package manager.investments;

public class InvestmentData {

    public static class InvestmentDataBuilder {
        private String ownerId;
        private String buyerId;
        private String loanId;
        private String investmentId;


        public InvestmentDataBuilder owner(String owner) {
            this.ownerId = owner;
            return this;
        }

        public InvestmentDataBuilder buyer(String buyer) {
            this.buyerId = buyer;
            return this;
        }

        public InvestmentDataBuilder loan(String loan) {
            this.loanId = loan;
            return this;
        }

        public InvestmentDataBuilder investment(String investment) {
            this.investmentId = investment;
            return this;
        }

        public InvestmentData build() {
            return new InvestmentData(this);
        }
    }

    private final String ownerId;
    private final String buyerId;
    private final String loanId;
    private final String investmentId;

    public InvestmentData(InvestmentDataBuilder builder) {
        this.ownerId = builder.ownerId;
        this.loanId = builder.loanId;
        this.investmentId = builder.investmentId;
        this.buyerId = builder.buyerId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getLoanId() {
        return loanId;
    }

    public String getInvestmentId() {
        return investmentId;
    }
}
