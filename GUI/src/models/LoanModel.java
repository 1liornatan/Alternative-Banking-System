package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoanModel {
    private StringProperty id, status;
    private IntegerProperty amount, startYaz, endYaz, nextPaymentInYaz, finalAmount;


    private LoanModel(LoanModelBuilder builder) {
        this.id = builder.id;
        this.amount = builder.amount;
        this.startYaz = builder.startYaz;
        this.endYaz = builder.endYaz;
        this.nextPaymentInYaz = builder.nextPaymentInYaz;
        this.finalAmount = builder.finalAmount;
        this.status = builder.status;
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public int getAmount() {
        return amount.get();
    }

    public IntegerProperty amountProperty() {
        return amount;
    }

    public int getStartYaz() {
        return startYaz.get();
    }

    public IntegerProperty startYazProperty() {
        return startYaz;
    }

    public int getEndYaz() {
        return endYaz.get();
    }

    public IntegerProperty endYazProperty() {
        return endYaz;
    }

    public int getNextPaymentInYaz() {
        return nextPaymentInYaz.get();
    }

    public IntegerProperty nextPaymentInYazProperty() {
        return nextPaymentInYaz;
    }

    public int getFinalAmount() {
        return finalAmount.get();
    }

    public IntegerProperty finalAmountProperty() {
        return finalAmount;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public static class LoanModelBuilder {
        private StringProperty id, status;
        private IntegerProperty amount, startYaz, endYaz, nextPaymentInYaz, finalAmount;

        public LoanModelBuilder() {
            id = new SimpleStringProperty();
            amount = new SimpleIntegerProperty();
            startYaz = new SimpleIntegerProperty();
            endYaz = new SimpleIntegerProperty();
            nextPaymentInYaz = new SimpleIntegerProperty();
            finalAmount = new SimpleIntegerProperty();
            status = new SimpleStringProperty();
        }

        public LoanModelBuilder id(String id) {
            this.id.set(id);
            return this;
        }

        public LoanModelBuilder amount(int amount) {
            this.amount.set(amount);
            return this;
        }

        public LoanModelBuilder status(String status) {
            this.status.set(status);
            return this;
        }

        public LoanModelBuilder startYaz(int startYaz) {
            this.startYaz.set(startYaz);
            return this;
        }

        public LoanModelBuilder endYaz(int endYaz) {
            this.endYaz.set(endYaz);
            return this;
        }

        public LoanModelBuilder nextPaymentInYaz(int nextPaymentInYaz) {
            this.nextPaymentInYaz.set(nextPaymentInYaz);
            return this;
        }

        public LoanModelBuilder finalAmount(int finalAmount) {
            this.finalAmount.set(finalAmount);
            return this;
        }

        public LoanModel build() {
            LoanModel loanModel = new LoanModel(this);
            return loanModel;
        }
    }


}
