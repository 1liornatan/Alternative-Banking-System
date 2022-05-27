package screens.customer;

import bank.impl.BankImpl;
import bank.loans.interest.exceptions.InvalidPercentException;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import manager.investments.RequestDTO;
import manager.loans.LoanData;
import manager.loans.LoansData;
import manager.transactions.TransactionData;
import models.LoanModel;
import models.NotificationModel;
import models.TransactionModel;
import models.utils.LoanTable;
import org.controlsfx.control.CheckComboBox;

import java.util.*;
import java.util.stream.Collectors;

public class CustomerController {

    private BankImpl bankInstance;
    private List<LoanModel> loanerModelList;
    private List<LoanModel> lenderModelList;
    private StringProperty customerId;
    private ObservableList<String> categoriesList;
    private BooleanProperty isFileSelected;
    private List<TransactionModel> transactionModels;
    private List<LoanModel> loanModelList;


    @FXML
    private TableView<LoanModel> loanerLoansTable;

    @FXML
    private TableView<LoanModel> lenderLoansTable;

    @FXML
    private TableView<TransactionModel> transactionsTable;

    @FXML
    private Button chargeButton;

    @FXML
    private Button withdrawButton;

    @FXML
    private Button searchLoansButton;

    @FXML
    private TableView<LoanModel> loanerLoansPTable;

    @FXML
    private TableView<?> paymentControls;

    @FXML
    private TableView<NotificationModel> notificationsTable;


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
    private TableView<LoanModel> loansFoundTable;

    @FXML
    private TableView<LoanModel> loansChosenTable;

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
        RequestDTO requestDTO = new RequestDTO
            .Builder(customerId.get(), Integer.valueOf(amountField.getText()))
            .categories(getSelectedCategories()) // TODO: apply optional options
/*                .minInterest(Integer.value)
            .minDuration(minLoanDuration)
            .maxLoans(maxRequestedLoans)*/
            .build();

        try {
            LoansData loansData = bankInstance.loanAssignmentRequest(requestDTO);
            addFoundLoans(loansData.getLoans());
        } catch (InvalidPercentException e) {
            e.printStackTrace();
        }
    }

    private void addFoundLoans(List<LoanData> loansList) {
        loansFoundTable.setItems(FXCollections.observableArrayList(makeLoanModelList(loansList)));
    }


    private List<LoanModel> makeLoanModelList(List<LoanData> loanDataList) {
        List<LoanModel> tempLoanModelList = new ArrayList<>();
        for(LoanData loanData : loanDataList) {
            LoanModel loanModel = new LoanModel.LoanModelBuilder()
                    .id(loanData.getName())
                    .amount(loanData.getBaseAmount())
                    .endYaz(loanData.getFinishedYaz())
                    .startYaz(loanData.getStartedYaz())
                    .nextPaymentInYaz(loanData.getNextPaymentInYaz())
                    .finalAmount(loanData.getFinalAmount()).build();

            tempLoanModelList.add(loanModel);
        }
        return tempLoanModelList;
    }


    private Set<String> getSelectedCategories() {

        ObservableList<String> checkedItems = categoriesComboBox.getCheckModel().getCheckedItems();

        return checkedItems.stream().collect(Collectors.toSet());
    }

    @FXML
    void tablesRightButtonAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
        LoanTable.setDataTables(loanerLoansTable);
        LoanTable.setDataTables(lenderLoansTable);
        LoanTable.setDataTables(loansFoundTable);
        LoanTable.setDataTables((loansChosenTable));
        LoanTable.setDataTables(loanerLoansPTable);
        setNotificationsTable();
        updateCategories();
        setTransactionsTable();
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
        transactionModels = new ArrayList<>();
        loanModelList = new ArrayList<>();
    }

    public void updateCategories() {
        if(!isFileSelected.get())
            return;

        Thread updateCategories = new Thread(() -> {
            Collection<String> categories = bankInstance.getCategoriesDTO().getCategories();
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

    public void updateLoansData() {
        Thread updateThread = new Thread(() -> {
            List<LoanModel> tempLenderModelList = new ArrayList<>();
            List<LoanModel> tempLoanerModelList = new ArrayList<>();
            List<LoanData>  loanerDataList = bankInstance.getLoanerData(customerId.get()).getLoans();
            List<LoanData>  lenderDataList = bankInstance.getInvestorData(customerId.get()).getLoans();

            updateList(loanerDataList, tempLoanerModelList);
            updateList(lenderDataList, tempLenderModelList);

            lenderModelList = tempLenderModelList;
            loanerModelList = tempLoanerModelList;

            Platform.runLater(() -> {
                loanerLoansTable.setItems(getLoans(loanerModelList));
                lenderLoansTable.setItems(getLoans(lenderModelList));
                loanerLoansPTable.setItems(getLoans(loanerModelList));
            });
        });

        updateThread.start();
    }

    private void updateList(List<LoanData> src, List<LoanModel> dest) {
        for(LoanData loanData : src) {
            LoanModel loanModel = new LoanModel.LoanModelBuilder()
                    .id(loanData.getName())
                    .amount(loanData.getBaseAmount())
                    .endYaz(loanData.getFinishedYaz())
                    .startYaz(loanData.getStartedYaz())
                    .nextPaymentInYaz(loanData.getNextPaymentInYaz())
                    .finalAmount(loanData.getFinalAmount()).build();;

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

    private ObservableList<TransactionModel> getTransactions() {
        return FXCollections.observableArrayList(transactionModels);
    }

    private ObservableList<LoanModel> getLoans() {
        return FXCollections.observableArrayList();
    }

    private void updateTransactions() {
        if(!isFileSelected.get())
            return;
        Thread updateTransactions = new Thread(() -> {
            List<TransactionData> transactionsData = bankInstance.getTransactionsData(customerId.get()).getTransactions();
            List<TransactionModel> tempTransactionModels = new ArrayList<>();
            for(TransactionData data : transactionsData) {
                tempTransactionModels.add(new TransactionModel.TransactionModelBuilder()
                        .description(data.getDescription())
                        .amount(data.getAmount())
                        .previousBalance(data.getPreviousBalance())
                        .yazMade(data.getYazMade())
                        .build());
            }
            transactionModels = tempTransactionModels;
            Platform.runLater(() -> transactionsTable.setItems(getTransactions()));
        });
        updateTransactions.start();
    }

    private void setTransactionsTable() {
        TableColumn<TransactionModel, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<TransactionModel, Integer> balanceColumn = new TableColumn<>("Amount");
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<TransactionModel, Integer> previousBalanceColumn = new TableColumn<>("Previous Balance");
        previousBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("previousBalance"));

        TableColumn<TransactionModel, Integer> yazMadeColumn = new TableColumn<>("Yaz Made");
        yazMadeColumn.setCellValueFactory(new PropertyValueFactory<>("yazMade"));

        transactionsTable.getColumns().addAll(descriptionColumn, balanceColumn, previousBalanceColumn, yazMadeColumn);

        updateTransactions();
        transactionsTable.setItems(getTransactions());

    }

    private void setNotificationsTable() {
        TableColumn<NotificationModel, String> notificationMessageColumn = new TableColumn<>("Message");
        notificationMessageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        notificationMessageColumn.setPrefWidth(300);
        TableColumn<NotificationModel, Integer> yazMadeColumn = new TableColumn<>("Made In Yaz");
        yazMadeColumn.setCellValueFactory(new PropertyValueFactory<>("yazMade"));

        notificationsTable.getColumns().addAll(notificationMessageColumn, yazMadeColumn);
    }
}

