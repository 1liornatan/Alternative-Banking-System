package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TransactionModel {
    private final StringProperty description;
    private final IntegerProperty amount, previousBalance, yazMade;

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public int getAmount() {
        return amount.get();
    }

    public IntegerProperty amountProperty() {
        return amount;
    }

    public int getPreviousBalance() {
        return previousBalance.get();
    }

    public IntegerProperty previousBalanceProperty() {
        return previousBalance;
    }

    public int getYazMade() {
        return yazMade.get();
    }

    public IntegerProperty yazMadeProperty() {
        return yazMade;
    }

    private TransactionModel(TransactionModelBuilder builder) {
        this.description = builder.description;
        this.amount = builder.amount;
        this.previousBalance = builder.previousBalance;
        this.yazMade = builder.yazMade;
    }

    public static class TransactionModelBuilder {
        private final StringProperty description;
        private final IntegerProperty amount, previousBalance, yazMade;

        public TransactionModelBuilder() {
            description = new SimpleStringProperty();
            amount = new SimpleIntegerProperty();
            previousBalance = new SimpleIntegerProperty();
            yazMade = new SimpleIntegerProperty();
        }

        public TransactionModelBuilder description( String description) {
            this.description.set(description);
            return this;
        }

        public TransactionModelBuilder amount( int amount) {
            this.amount.set(amount);
            return this;
        }

        public TransactionModelBuilder previousBalance( int previousBalance) {
            this.previousBalance.set(previousBalance);
            return this;
        }

        public TransactionModelBuilder yazMade( int yazMade) {
            this.yazMade.set(yazMade);
            return this;
        }

        public TransactionModel build() {
            TransactionModel transactionModel = new TransactionModel(this);
            return transactionModel;
        }

    }
}
