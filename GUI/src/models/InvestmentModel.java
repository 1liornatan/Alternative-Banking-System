package models;

import javafx.beans.property.*;

public class InvestmentModel {
    final StringProperty loanId, ownerId, investmentId;
    final IntegerProperty yazMade, amount;
    final BooleanProperty isForSale;

    private InvestmentModel(InvestmentModelBuilder builder) {
        loanId = builder.loanId;
        ownerId = builder.ownerId;
        yazMade = builder.yazMade;
        amount = builder.amount;
        investmentId = builder.investmentId;
        isForSale = builder.isForSale;
    }

    public static class InvestmentModelBuilder {
        final StringProperty loanId;
        final StringProperty ownerId;
        final StringProperty investmentId;
        final IntegerProperty yazMade;
        final IntegerProperty amount;
        final BooleanProperty isForSale;

        public InvestmentModelBuilder() {
            loanId = new SimpleStringProperty();
            ownerId = new SimpleStringProperty();
            yazMade = new SimpleIntegerProperty();
            amount = new SimpleIntegerProperty();
            investmentId = new SimpleStringProperty();
            isForSale = new SimpleBooleanProperty(false);
        }

        public InvestmentModelBuilder id(String investmentId) {
            this.investmentId.set(investmentId);
            return this;
        }
        public InvestmentModelBuilder loan(String loanId) {
            this.loanId.set(loanId);
            return this;
        }

        public InvestmentModelBuilder owner(String ownerId) {
            this.ownerId.set(ownerId);
            return this;
        }

        public InvestmentModelBuilder amount(Integer amount) {
            this.amount.set(amount);
            return this;
        }

        public InvestmentModelBuilder yaz(Integer yazMade) {
            this.yazMade.set(yazMade);
            return this;
        }

        public InvestmentModelBuilder forSale(Boolean isForSale) {
            this.isForSale.set(isForSale);
            return this;
        }

        public InvestmentModel build() {
            return new InvestmentModel(this);
        }
    }

    public String getLoanId() {
        return loanId.get();
    }

    public StringProperty loanIdProperty() {
        return loanId;
    }

    public String getOwnerId() {
        return ownerId.get();
    }

    public StringProperty ownerIdProperty() {
        return ownerId;
    }

    public int getYazMade() {
        return yazMade.get();
    }

    public IntegerProperty yazMadeProperty() {
        return yazMade;
    }

    public int getAmount() {
        return amount.get();
    }

    public IntegerProperty amountProperty() {
        return amount;
    }

    public String getInvestmentId() {
        return investmentId.get();
    }

    public StringProperty investmentIdProperty() {
        return investmentId;
    }

    public boolean isIsForSale() {
        return isForSale.get();
    }

    public BooleanProperty isForSaleProperty() {
        return isForSale;
    }
}
