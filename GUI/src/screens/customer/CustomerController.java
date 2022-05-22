package screens.customer;

import bank.impl.BankImpl;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import manager.loans.LoanData;
import models.LoanModel;
import models.utils.LoanTable;

import java.util.ArrayList;
import java.util.List;

public class CustomerController {

    private BankImpl bankInstance;
    private List<LoanModel> loanerModelList;
    private List<LoanModel> lenderModelList;
    private StringProperty customerId;


    @FXML
    private TableView<LoanModel> loanerLoansTable;

    @FXML
    private TableView<LoanModel> lenderLoansTable;

    @FXML
    private TableView<?> transactionsTable;

    @FXML
    private Button chargeButton;

    @FXML
    private Button withdrawButton;

    @FXML
    private TableView<?> loanerLoansPTable;

    @FXML
    private TableView<?> paymentControls;

    @FXML
    private TableView<?> notificationsTable;

    @FXML
    void initialize() {
        LoanTable.setDataTables(loanerLoansTable);
        LoanTable.setDataTables(lenderLoansTable);
    }

    public String getCustomerId() {
        return customerId.get();
    }

    public StringProperty customerIdProperty() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId.set(customerId);
    }

    public CustomerController() {
        customerId = new SimpleStringProperty();
    }
    public void updateLoansData() {
        Thread updateThread = new Thread(() -> {
            List<LoanModel> tempLenderModelList = new ArrayList<>();
            List<LoanModel> tempLoanerModelList = new ArrayList<>();
            List<LoanData>  loanerDataList = bankInstance.getLoanerData(customerId.get()).getLoans();
            List<LoanData>  lenderDataList = bankInstance.getLenderData(customerId.get()).getLoans();

            updateList(loanerDataList, tempLoanerModelList);
            updateList(lenderDataList, tempLenderModelList);

            lenderModelList = tempLenderModelList;
            loanerModelList = tempLoanerModelList;

            Platform.runLater(() -> {
                loanerLoansTable.setItems(getLoans(loanerModelList));
                lenderLoansTable.setItems(getLoans(lenderModelList));
            });
        });

        updateThread.start();
    }

    private ObservableList<LoanModel> getLoans(List<LoanModel> modelList) {
        return FXCollections.observableArrayList(modelList);
    }

    private void updateList(List<LoanData> src, List<LoanModel> dest) {
        for(LoanData loanData : src) {
            LoanModel loanModel = new LoanModel();

            loanModel.setId(loanData.getName());
            loanModel.setAmount(loanData.getBaseAmount());
            loanModel.setEndYaz(loanData.getFinishedYaz());
            loanModel.setStartYaz(loanData.getStartedYaz());

            dest.add(loanModel);
        }
    }
    public BankImpl getBankInstance() {
        return bankInstance;
    }

    public void setBankInstance(BankImpl bankInstance) {
        this.bankInstance = bankInstance;
    }
}
