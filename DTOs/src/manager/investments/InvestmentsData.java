package manager.investments;

import java.util.ArrayList;
import java.util.List;

public class InvestmentsData {
    public static class InvestmentsBuilder {
        String investorId;
        int amount;

        List<String> loansIds;

        public InvestmentsBuilder Amount(int amount) {
            this.amount = amount;
            return this;
        }

        public InvestmentsBuilder Name(String name) {
            investorId = name;
            return this;
        }

        public InvestmentsBuilder Loans(List<String> loansIds) {
            this.loansIds = loansIds;
            return this;
        }
        public InvestmentsBuilder AddLoan(String loanId) {
            loansIds.add(loanId);
            return this;
        }

        public InvestmentsBuilder() {
            loansIds = new ArrayList<>();
        }

        public InvestmentsData Build() {
            return new InvestmentsData(this);
        }
    }

    final int amount;
    final String investorId;
    final List<String> loansIds;

    private InvestmentsData(InvestmentsBuilder investmentsBuilder) {
        amount = investmentsBuilder.amount;
        investorId = investmentsBuilder.investorId;
        loansIds = investmentsBuilder.loansIds;
    }

    public int getAmount() {
        return amount;
    }

    public String getInvestorId() {
        return investorId;
    }

    public List<String> getLoansIds() {
        return loansIds;
    }

}
