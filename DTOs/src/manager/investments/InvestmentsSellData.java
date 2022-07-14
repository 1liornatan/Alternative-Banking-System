package manager.investments;

import java.util.ArrayList;
import java.util.List;

public class InvestmentsSellData {
    public static class SellBuilder {
        List<String> investorsIds;
        List<Integer> amounts;
        List<String> loansIds;
        List<Integer> yazPlaced;
        List<String> invIds;
        List<Boolean> forSale;
        int version;

        public SellBuilder forSale(List<Boolean> forSale) {
            this.forSale = forSale;
            return this;
        }
        public SellBuilder id(List<String> investmentIds) {
            this.invIds = investmentIds;
            return this;
        }
        public SellBuilder version(int version) {
            this.version = version;
            return this;
        }

        public SellBuilder amount(List<Integer> amounts) {
            this.amounts = amounts;
            return this;
        }

        public SellBuilder time(List<Integer> time) {
            this.yazPlaced = time;
            return this;
        }

        public SellBuilder name(List<String> names) {
            investorsIds = names;
            return this;
        }

        public SellBuilder loans(List<String> loansIds) {
            this.loansIds = loansIds;
            return this;
        }

        public SellBuilder() {
            loansIds = new ArrayList<>();
            investorsIds = new ArrayList<>();
            amounts = new ArrayList<>();
            invIds = new ArrayList<>();
            forSale = new ArrayList<>();
            yazPlaced = new ArrayList<>();
            version = 0;
        }

        public InvestmentsSellData Build() {
            return new InvestmentsSellData(this);
        }
    }

    final List<String> investorsIds;
    final List<Integer> amounts;
    final List<String> loansIds;
    final List<Integer> yazPlaced;
    final List<String> invIds;
    final List<Boolean> forSale;
    final int version;

    private InvestmentsSellData(SellBuilder investmentsBuilder) {
        this.amounts = investmentsBuilder.amounts;
        this.investorsIds = investmentsBuilder.investorsIds;
        this.loansIds = investmentsBuilder.loansIds;
        this.yazPlaced = investmentsBuilder.yazPlaced;
        this.invIds = investmentsBuilder.invIds;
        this.forSale = investmentsBuilder.forSale;
        this.version = investmentsBuilder.version;
    }

    public List<Integer> getYazPlaced() {
        return yazPlaced;
    }

    public List<String> getInvestorsIds() {
        return investorsIds;
    }

    public List<Integer> getAmounts() {
        return amounts;
    }

    public List<String> getLoansIds() {
        return loansIds;
    }

    public List<String> getInvIds() {
        return invIds;
    }

    public List<Boolean> getForSale() {
        return forSale;
    }

    public int getVersion() {return version;}

}
