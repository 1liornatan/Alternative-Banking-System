package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomerModel {

    final IntegerProperty balance;
    final StringProperty name;
    final IntegerProperty numOfPendingLoansRequested;
    final IntegerProperty numOfActiveLoansRequested;
    final IntegerProperty numOfRiskLoansRequested;
    final IntegerProperty numOfFinishedLoansRequested;
    final IntegerProperty numOfNewLoansRequested;
    final IntegerProperty numOfPendingLoansInvested;
    final IntegerProperty numOfActiveLoansInvested;
    final IntegerProperty numOfRiskLoansInvested;
    final IntegerProperty numOfFinishedLoansInvested;
    final IntegerProperty numOfNewLoansInvested;

    public CustomerModel() {
        balance = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        numOfActiveLoansInvested = new SimpleIntegerProperty();
        numOfActiveLoansRequested = new SimpleIntegerProperty();
        numOfPendingLoansInvested = new SimpleIntegerProperty();
        numOfPendingLoansRequested = new SimpleIntegerProperty();
        numOfRiskLoansInvested = new SimpleIntegerProperty();
        numOfRiskLoansRequested = new SimpleIntegerProperty();
        numOfFinishedLoansInvested = new SimpleIntegerProperty();
        numOfFinishedLoansRequested = new SimpleIntegerProperty();
        numOfNewLoansInvested = new SimpleIntegerProperty();
        numOfNewLoansRequested = new SimpleIntegerProperty();
    }

    public int getBalance() {
        return balance.get();
    }

    public IntegerProperty balanceProperty() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance.set(balance);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
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

    public int getNumOfPendingLoansInvested() {
        return numOfPendingLoansInvested.get();
    }

    public IntegerProperty numOfPendingLoansInvestedProperty() {
        return numOfPendingLoansInvested;
    }

    public void setNumOfPendingLoansInvested(int numOfPendingLoansInvested) {
        this.numOfPendingLoansInvested.set(numOfPendingLoansInvested);
    }

    public int getNumOfActiveLoansInvested() {
        return numOfActiveLoansInvested.get();
    }

    public IntegerProperty numOfActiveLoansInvestedProperty() {
        return numOfActiveLoansInvested;
    }

    public void setNumOfActiveLoansInvested(int numOfActiveLoansInvested) {
        this.numOfActiveLoansInvested.set(numOfActiveLoansInvested);
    }

    public int getNumOfRiskLoansInvested() {
        return numOfRiskLoansInvested.get();
    }

    public IntegerProperty numOfRiskLoansInvestedProperty() {
        return numOfRiskLoansInvested;
    }

    public void setNumOfRiskLoansInvested(int numOfRiskLoansInvested) {
        this.numOfRiskLoansInvested.set(numOfRiskLoansInvested);
    }

    public int getNumOfFinishedLoansInvested() {
        return numOfFinishedLoansInvested.get();
    }

    public IntegerProperty numOfFinishedLoansInvestedProperty() {
        return numOfFinishedLoansInvested;
    }

    public void setNumOfFinishedLoansInvested(int numOfFinishedLoansInvested) {
        this.numOfFinishedLoansInvested.set(numOfFinishedLoansInvested);
    }

    public int getNumOfNewLoansInvested() {
        return numOfNewLoansInvested.get();
    }

    public IntegerProperty numOfNewLoansInvestedProperty() {
        return numOfNewLoansInvested;
    }

    public void setNumOfNewLoansInvested(int numOfNewLoansInvested) {
        this.numOfNewLoansInvested.set(numOfNewLoansInvested);
    }



}
