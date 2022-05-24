package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TransactionModel {
    private StringProperty description;
    private IntegerProperty amount, previousBalance, yazMade;

    private TransactionModel(TransactionModelBuilder builder) {
        this.description = builder.description;
        this.amount = builder.amount;
        this.previousBalance = builder.previousBalance;
        this.yazMade = builder.yazMade;
    }

    public static class TransactionModelBuilder {
        private StringProperty description;
        private IntegerProperty amount, previousBalance, yazMade;

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
