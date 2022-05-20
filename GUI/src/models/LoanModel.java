package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoanModel {
    StringProperty id;
    IntegerProperty amount, startYaz, endYaz;

    public LoanModel() {
        id = new SimpleStringProperty();
        amount = new SimpleIntegerProperty();
        startYaz = new SimpleIntegerProperty();
        endYaz = new SimpleIntegerProperty();
    }
    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public int getAmount() {
        return amount.get();
    }

    public IntegerProperty amountProperty() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    public int getStartYaz() {
        return startYaz.get();
    }

    public IntegerProperty startYazProperty() {
        return startYaz;
    }

    public void setStartYaz(int startYaz) {
        this.startYaz.set(startYaz);
    }

    public int getEndYaz() {
        return endYaz.get();
    }

    public IntegerProperty endYazProperty() {
        return endYaz;
    }

    public void setEndYaz(int endYaz) {
        this.endYaz.set(endYaz);
    }
}
