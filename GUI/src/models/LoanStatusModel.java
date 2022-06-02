package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoanStatusModel {
    final IntegerProperty numOfPendingLoansRequested;
    final IntegerProperty numOfActiveLoansRequested;
    final IntegerProperty numOfRiskLoansRequested;
    final IntegerProperty numOfFinishedLoansRequested;
    final IntegerProperty numOfNewLoansRequested;
    final StringProperty name;

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public LoanStatusModel(String name, int statusActive, int statusPending, int statusNew, int statusRisk, int statusFinished) {
        numOfActiveLoansRequested = new SimpleIntegerProperty(statusActive);
        numOfPendingLoansRequested = new SimpleIntegerProperty(statusPending);
        numOfRiskLoansRequested = new SimpleIntegerProperty(statusRisk);
        numOfFinishedLoansRequested = new SimpleIntegerProperty(statusFinished);
        numOfNewLoansRequested = new SimpleIntegerProperty(statusNew);
        this.name = new SimpleStringProperty(name);
    }

    public int getNumOfPendingLoansRequested() {
        return numOfPendingLoansRequested.get();
    }

    public IntegerProperty numOfPendingLoansRequestedProperty() {
        return numOfPendingLoansRequested;
    }

    public void setNumOfPendingLoansRequested(int numOfPendingLoansRequested) {
        this.numOfPendingLoansRequested.set(numOfPendingLoansRequested);
    }

    public int getNumOfActiveLoansRequested() {
        return numOfActiveLoansRequested.get();
    }

    public IntegerProperty numOfActiveLoansRequestedProperty() {
        return numOfActiveLoansRequested;
    }

    public void setNumOfActiveLoansRequested(int numOfActiveLoansRequested) {
        this.numOfActiveLoansRequested.set(numOfActiveLoansRequested);
    }

    public int getNumOfRiskLoansRequested() {
        return numOfRiskLoansRequested.get();
    }

    public IntegerProperty numOfRiskLoansRequestedProperty() {
        return numOfRiskLoansRequested;
    }

    public void setNumOfRiskLoansRequested(int numOfRiskLoansRequested) {
        this.numOfRiskLoansRequested.set(numOfRiskLoansRequested);
    }

    public int getNumOfFinishedLoansRequested() {
        return numOfFinishedLoansRequested.get();
    }

    public IntegerProperty numOfFinishedLoansRequestedProperty() {
        return numOfFinishedLoansRequested;
    }

    public void setNumOfFinishedLoansRequested(int numOfFinishedLoansRequested) {
        this.numOfFinishedLoansRequested.set(numOfFinishedLoansRequested);
    }

    public int getNumOfNewLoansRequested() {
        return numOfNewLoansRequested.get();
    }

    public IntegerProperty numOfNewLoansRequestedProperty() {
        return numOfNewLoansRequested;
    }

    public void setNumOfNewLoansRequested(int numOfNewLoansRequested) {
        this.numOfNewLoansRequested.set(numOfNewLoansRequested);
    }
}
