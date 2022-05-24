package screens.customer;

import bank.impl.BankImpl;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import manager.categories.CategoriesDTO;
import manager.loans.LoanData;
import models.LoanModel;
import models.utils.LoanTable;
import org.controlsfx.control.CheckComboBox;

import java.util.ArrayList;
import java.util.List;

public class CustomerController {

    private BankImpl bankInstance;
    private List<LoanModel> loanerModelList;
    private List<LoanModel> lenderModelList;
    private StringProperty customerId;
    private ObservableList<String> categoriesList;
    private BooleanProperty isFileSelected;


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
    private Button searchLoansButton;

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
        updateCategories();
        amountField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    amountField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        minInterestField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    minInterestField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        minYazField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    minYazField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        maxLoanerLoansField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    maxLoanerLoansField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        maxOwnershipField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    maxOwnershipField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    private TextField amountField;

    @FXML
    private TextField minInterestField;

    @FXML
    private TextField minYazField;

    @FXML
    private TextField maxLoanerLoansField;

    @FXML
    private TextField maxOwnershipField;

    @FXML
    private CheckComboBox<String> categoriesComboBox;

    @FXML
    private TableView<?> loansFoundTable;

    @FXML
    private TableView<?> loansChosenTable;

    @FXML
    private Button tablesRightButton;

    @FXML
    private Button tablesLeftButton;

    @FXML
    private Button investButton;

    @FXML
    void investButtonAction(ActionEvent event) {
        updateCategories();
    }

    @FXML
    void tablesLeftButtonAction(ActionEvent event) {

    }

    @FXML
    void searchLoansButtonAction(ActionEvent event) {

    }

    @FXML
    void tablesRightButtonAction(ActionEvent event) {

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
        isFileSelected = new SimpleBooleanProperty();
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

    public void updateCategories() {
        if(!isFileSelected.get())
            return;

        Thread updateCategories = new Thread(() -> {
            List<String> categories = bankInstance.getCategoriesDTO().getCategories();
            ObservableList<String> tempCategoriesList = FXCollections.observableArrayList();
            for(String category : categories) {
                tempCategoriesList.add(category);
            }
            categoriesList = tempCategoriesList;
            Platform.runLater(() -> categoriesComboBox.getItems().setAll(categoriesList));
        });
        updateCategories.start();
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

    private void searchLoans() {
        Integer amount = Integer.valueOf(amountField.getText());
    }

    public BankImpl getBankInstance() {
        return bankInstance;
    }

    public void setBankInstance(BankImpl bankInstance) {
        this.bankInstance = bankInstance;
    }

    public boolean isIsFileSelected() {
        return isFileSelected.get();
    }

    public BooleanProperty isFileSelectedProperty() {
        return isFileSelected;
    }

    public void setIsFileSelected(boolean isFileSelected) {
        this.isFileSelected.set(isFileSelected);
    }
}

