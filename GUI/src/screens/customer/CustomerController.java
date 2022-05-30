package screens.customer;

import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.impl.BankImpl;
import bank.impl.exceptions.DataNotFoundException;
import bank.loans.interest.exceptions.InvalidPercentException;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import manager.investments.InvestmentData;
import manager.investments.InvestmentsData;
import manager.investments.InvestmentsSellData;
import manager.investments.RequestDTO;
import manager.loans.LoanData;
import manager.loans.LoansData;
import manager.messages.NotificationData;
import manager.transactions.TransactionData;
import models.InvestmentModel;
import models.LoanModel;
import models.NotificationModel;
import models.TransactionModel;
import models.utils.LoanTable;
import models.utils.TradeTable;
import org.controlsfx.control.CheckComboBox;

import java.util.*;
import java.util.stream.Collectors;

public class CustomerController {

    private BankImpl bankInstance;
    private List<LoanModel> loanerModelList;
    private List<LoanModel> lenderModelList;
    private StringProperty customerId;
    private IntegerProperty investAmount;
    private IntegerProperty balanceProperty;
    private IntegerProperty minInterestProperty;
    private IntegerProperty minLoanDurationProperty;
    private IntegerProperty maxRequestedLoansProperty;
    private IntegerProperty maxOwnershipFieldProperty;
    private ObservableList<String> categoriesList;
    private BooleanProperty isFileSelected;
    private List<TransactionModel> transactionModels;
    private List<LoanModel> loanPModelList;
    private List<NotificationModel> notificationModels;
    private List<InvestmentModel> buyInvestmentModels;
    private List<InvestmentModel> sellInvestmentModels;
    private IntegerProperty requestedLoansAmountProperty;
    private IntegerProperty paymentsAmountProperty;

    @FXML
    private TextField balanceField;

    @FXML
    private TextField openPaymentsField;

    @FXML
    private TextField loansRequestedField;

    @FXML
    private TableView<LoanModel> loanerLoansTable;

    @FXML
    private TableView<LoanModel> lenderLoansTable;

    @FXML
    private TableView<TransactionModel> transactionsTable;

    @FXML
    private Button chargeButton;

    @FXML
    private ProgressBar searchLoansProgressBar;

    @FXML
    private Label progressBarStatusLabel;

    @FXML
    private Button withdrawButton;

    @FXML
    private Button searchLoansButton;

    @FXML
    private TableView<LoanModel> loanerLoansPTable;

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
    private Button payCycleButton;

    @FXML
    private Button closeLoanButton;

    @FXML
    private Button payDebtButton;

    @FXML
    private Button submitDebtButton;

    @FXML
    private HBox debtPaymentHBox;

    @FXML
    private TextField debtAmountField;

    @FXML
    private TableView<InvestmentModel> buyInvestmentTable;

    @FXML
    private Button searchInvestmentButton;

    @FXML
    private Button buyInvestmentButton;

    @FXML
    private Label buyErrorLabel;

    @FXML
    private Label paymentErrorLabel;

    @FXML
    private TableView<InvestmentModel> sellInvestmentTable;

    @FXML
    private Button listInvestmentButton;

    @FXML
    private Button unlistInvestmentButton;

    @FXML
    private Label sellErrorLabel;

    @FXML
    void buyInvestmentButtonAction(ActionEvent event) {
        InvestmentModel selectedItem = buyInvestmentTable.getSelectionModel().getSelectedItem();
        if(selectedItem == null) {
            buyErrorLabel.setText("Please select an investment!");
            buyErrorLabel.setTextFill(Color.RED);
        }
        else {
            InvestmentData data = new InvestmentData.InvestmentDataBuilder()
                    .loan(selectedItem.getLoanId())
                    .buyer(customerId.get())
                    .owner(selectedItem.getOwnerId())
                    .investment(selectedItem.getInvestmentId())
                    .build();

            try {
                bankInstance.investmentTrade(data);
                Platform.runLater(() -> {
                    updateData();
                    buyInvestmentTable.getItems().remove(selectedItem);
                    buyErrorLabel.setText("Investment bought successfully!");
                    buyErrorLabel.setTextFill(Color.GREEN);
                });
            } catch (DataNotFoundException | NonPositiveAmountException | NoMoneyException e) {
                buyErrorLabel.setText(e.getMessage());
                buyErrorLabel.setTextFill(Color.RED);
            }
        }
    }

    @FXML
    void investButtonAction(ActionEvent event) {
        setInvestmentsChosen();
    }


    @FXML
    void listInvestmentButtonAction(ActionEvent event) {
        InvestmentModel selectedItem = sellInvestmentTable.selectionModelProperty().get().getSelectedItem();
        if(selectedItem == null) {
            sellErrorLabel.setText("Please select an investment!");
            sellErrorLabel.setTextFill(Color.RED);
        }
        else if(selectedItem.isIsForSale()) {
            sellErrorLabel.setText("Investment is already listed!");
            sellErrorLabel.setTextFill(Color.RED);
        }
        else {
            String id = selectedItem.getInvestmentId();
            String loanId = selectedItem.getLoanId();
            Thread listInvestmentThread = new Thread(() -> {
                InvestmentData data = new InvestmentData.InvestmentDataBuilder()
                        .investment(id)
                        .loan(loanId)
                        .build();
                try {
                    bankInstance.listInvestment(data);
                    Platform.runLater(() -> {
                        updateOwnedInvestments();
                        sellErrorLabel.setText("Listed investment successfully!");
                        sellErrorLabel.setTextFill(Color.GREEN);
                    });
                } catch (DataNotFoundException e) {
                    Platform.runLater(() -> {
                        sellErrorLabel.setText(e.getMessage());
                        sellErrorLabel.setTextFill(Color.RED);
                    });

                }
            });
            listInvestmentThread.start();
        }
    }

    @FXML
    void searchInvestmentButtonAction(ActionEvent event) {
        Thread searchInvestmentsThread = new Thread(() -> {
            InvestmentsSellData investmentsForSell = bankInstance.getInvestmentsForSell(customerId.get());
            List<InvestmentModel> tempInvList = getInvModels(investmentsForSell);

            buyInvestmentModels = tempInvList;

            Platform.runLater(() -> {
                buyInvestmentTable.setItems(getBuyInvestments());
            });

        });
        searchInvestmentsThread.start();
    }

    private ObservableList<InvestmentModel> getBuyInvestments() {
        return FXCollections.observableArrayList(buyInvestmentModels);
    }

    @FXML
    void unlistInvestmentButtonAction(ActionEvent event) {
        InvestmentModel selectedItem = sellInvestmentTable.selectionModelProperty().get().getSelectedItem();
        if(selectedItem == null) {
            sellErrorLabel.setText("Please select an investment!");
            sellErrorLabel.setTextFill(Color.RED);
        }
        else if(!selectedItem.isIsForSale()) {
            sellErrorLabel.setText("Investment is already unlisted!");
            sellErrorLabel.setTextFill(Color.RED);
        }
        else {
            Thread unlistInvestmentThread = new Thread(() -> {
                InvestmentData data = new InvestmentData.InvestmentDataBuilder()
                        .investment(selectedItem.getInvestmentId())
                        .build();

                try {
                    bankInstance.unlistInvestment(data);
                    Platform.runLater(() -> {
                        updateOwnedInvestments();
                        sellErrorLabel.setText("Unlisted investment successfully!");
                        sellErrorLabel.setTextFill(Color.GREEN);
                    });
                } catch (DataNotFoundException e) {
                    Platform.runLater(() -> {
                        sellErrorLabel.setText(e.getMessage());
                        sellErrorLabel.setTextFill(Color.RED);
                    });
                }
            });

            unlistInvestmentThread.start();
        }
    }


    @FXML
    void tablesLeftButtonAction(ActionEvent event) {
        ObservableList<LoanModel> selectedItems = loansChosenTable.getSelectionModel().getSelectedItems();
        loansFoundTable.getItems().addAll(selectedItems);
        loansChosenTable.getItems().removeAll(selectedItems);
    }

    @FXML
    void searchLoansButtonAction(ActionEvent event) {
        investAmount.set(Integer.valueOf(amountField.getText()));
        minInterestProperty.set(Integer.valueOf(minInterestField.getText()));
        minLoanDurationProperty.set(Integer.valueOf(minYazField.getText()));
        maxOwnershipFieldProperty.set(Integer.valueOf(maxOwnershipField.getText()));
        maxRequestedLoansProperty.set(Integer.valueOf(maxLoanerLoansField.getText()));
        searchLoansProgressBar.setProgress(0);

        if(investAmount.get() <= 0) {
            progressBarStatusLabel.textProperty().unbind();
            progressBarStatusLabel.setText("Amount must be positive!");
            progressBarStatusLabel.setTextFill(Color.RED);
            return;
        }

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Platform.runLater(() -> {
                    searchLoansButton.setDisable(true);
                    searchLoansProgressBar.setProgress(0);
                });
                updateMessage("Gathering Information...");
                RequestDTO requestDTO = new RequestDTO
                        .Builder(customerId.get(), investAmount.get())
                        .categories(getSelectedCategories()) // TODO: apply optional options
                        .minInterest(minInterestProperty.get())
                        .minDuration(minLoanDurationProperty.get())
                        .maxLoans(maxRequestedLoansProperty.get())
                        .build();

                try {
                    Thread.sleep(2000);
                    Platform.runLater(() -> searchLoansProgressBar.setProgress(0.3));
                    updateMessage("Searching Loans...");
                    LoansData loansData = bankInstance.loanAssignmentRequest(requestDTO);
                    Thread.sleep(1000);
                    Platform.runLater(() -> searchLoansProgressBar.setProgress(0.7));
                    updateMessage("Printing Loans...");
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        clearAllFields();
                        addFoundLoans(loansData.getLoans());
                        setLoansIntegrationButtons();
                        searchLoansProgressBar.setProgress(1.0);
                    });
                    updateMessage("Found " + loansData.getLoans().size() + " Loans");
                    return "Success";
                } catch (InvalidPercentException e) {
                    return e.getMessage();
                } finally {
                    Platform.runLater(() -> searchLoansButton.setDisable(false));
                }
            }
        };
        progressBarStatusLabel.textProperty().bind(task.messageProperty());
        progressBarStatusLabel.setTextFill(Color.BLUE);
        Thread findLoans = new Thread(task);
        findLoans.start();

    }

    @FXML
    void tablesRightButtonAction(ActionEvent event) {
        ObservableList<LoanModel> selectedItems = loansFoundTable.getSelectionModel().getSelectedItems();
        loansChosenTable.getItems().addAll(selectedItems);
        loansFoundTable.getItems().removeAll(selectedItems);
    }

    @FXML
    void closeLoanButtonAction(ActionEvent event) {

    }

    @FXML
    void payCycleButtonAction(ActionEvent event) {
        LoanModel selectedLoan = loanerLoansPTable.getSelectionModel().getSelectedItem();
        try {
            bankInstance.advanceOneCycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateData();
    }

    @FXML
    void payDebtButtonAction(ActionEvent event) {
        debtPaymentHBox.setDisable(false);
    }

    @FXML
    void submitDebtButtonAction(ActionEvent event) {
        LoanModel selectedLoan = loanerLoansPTable.getSelectionModel().getSelectedItem();
        try {
            bankInstance.deriskLoanRequest(selectedLoan.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateData();
    }

    @FXML
    void initialize() {
        setDataTables();
        setSplitComps();
        amountField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    amountField.setText(newValue.replaceAll("[^\\d]", ""));
                } else if (newValue.isEmpty()) {
                    amountField.setText("0");
                } else if (Integer.valueOf(newValue) > balanceProperty.get()) {
                    amountField.setText(String.valueOf(balanceProperty.get()));
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
                else if(Integer.valueOf(newValue) < 0) {
                    minInterestField.setText("0");
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
                else if(Integer.valueOf(newValue) < 1) {
                    minYazField.setText("1");
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
        debtAmountField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    debtAmountField.setText(newValue.replaceAll("[^\\d]", ""));
                } else if (newValue.isEmpty()) {
                    debtAmountField.setText("0");
                } else if (Integer.valueOf(newValue) > balanceProperty.get()) {
                    debtAmountField.setText(String.valueOf(balanceProperty.get()));
                }
            }
        });

        amountField.setText("0");
        debtAmountField.setText("0");
        minInterestField.setText("1");
        minYazField.setText("0");
        maxOwnershipField.setText("0");
        maxLoanerLoansField.setText("0");

        investButton.setDisable(true);
        tablesLeftButton.setDisable(true);
        tablesRightButton.setDisable(true);
        setLoansIntegrationButtons();
        debtPaymentHBox.setDisable(true);

        loansChosenTable.minWidthProperty().bind(loansFoundTable.widthProperty());
    }

    private void setSplitComps() {
        balanceField.textProperty().bind(balanceProperty.asString());
        loansRequestedField.textProperty().bind(requestedLoansAmountProperty.asString());
        openPaymentsField.textProperty().bind(paymentsAmountProperty.asString());
    }

    private void setDataTables() {
        LoanTable.setDataTables(loanerLoansTable);
        LoanTable.setDataTables(lenderLoansTable);
        LoanTable.setDataTables(loansFoundTable);
        LoanTable.setDataTables((loansChosenTable));
        LoanTable.setDataTables(loanerLoansPTable);
        TradeTable.setDataTable(buyInvestmentTable);
        TradeTable.setDataTable(sellInvestmentTable);

        TableColumn<InvestmentModel, Boolean> isListed = new TableColumn<>("Listed");
        isListed.setCellValueFactory(new PropertyValueFactory<>("isForSale"));
        isListed.setStyle("-fx-alignment: CENTER;");

        sellInvestmentTable.getColumns().add(isListed);
        sellInvestmentTable.getColumns().get(3).minWidthProperty().bind(sellInvestmentTable.widthProperty().multiply(0.1));
        isListed.minWidthProperty().bind(sellInvestmentTable.widthProperty().multiply(0.1));

        setNotificationsTable();
        setTransactionsTable();

    }

    public CustomerController() {
        customerId = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty();
        transactionModels = new ArrayList<>();
        loanPModelList = new ArrayList<>();
        investAmount = new SimpleIntegerProperty();
        balanceProperty = new SimpleIntegerProperty();
        minInterestProperty = new SimpleIntegerProperty();
        minLoanDurationProperty = new SimpleIntegerProperty();
        maxRequestedLoansProperty = new SimpleIntegerProperty();
        maxOwnershipFieldProperty = new SimpleIntegerProperty();
        buyInvestmentModels = new ArrayList<>();
        sellInvestmentModels = new ArrayList<>();
        requestedLoansAmountProperty = new SimpleIntegerProperty();
        paymentsAmountProperty = new SimpleIntegerProperty();
    }

    private void setLoansIntegrationButtons() {
        BooleanBinding emptyChosen = Bindings.isEmpty(loansChosenTable.getItems());
        BooleanBinding emptyFound = Bindings.isEmpty(loansFoundTable.getItems());
        investButton.disableProperty().bind(emptyChosen);
        tablesRightButton.disableProperty().bind(emptyFound);
        tablesLeftButton.disableProperty().bind(emptyChosen);
    }

    private void addFoundLoans(List<LoanData> loansList) {
        loansFoundTable.setItems(FXCollections.observableArrayList(makeLoanModelList(loansList)));
    }

    private void setInvestmentsChosen() {
        Thread setInvestmentsThread = new Thread(() -> {
            int amount = investAmount.get();
            List<String> selectedLoansIds = new ArrayList<>();
            loansChosenTable.getItems().stream().forEach(loanModel -> {
                selectedLoansIds.add(loanModel.getId());
            });
            InvestmentsData investmentsData = new InvestmentsData.InvestmentsBuilder()
                    .Name(customerId.get())
                    .Amount(amount)
                    .Loans(selectedLoansIds)
                    .Build();

            try {
                bankInstance.setInvestmentsData(investmentsData);
                Platform.runLater(() -> {
                    clearAllFields();
                    updateData();
                });
                // TODO: LABEL WITH STATUS & ERROR MESSAGE
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }
        });
        setInvestmentsThread.start();
    }

    private void clearAllFields() {
        loansChosenTable.getItems().clear();
        loansFoundTable.getItems().clear();
        buyInvestmentTable.getItems().clear();
        sellInvestmentTable.getItems().clear();
        amountField.setText("0");
        categoriesComboBox.getCheckModel().checkAll();
        sellErrorLabel.setText("");
        buyErrorLabel.setText("");

    }

    public void accountChanged() {
        updateData();
        progressBarStatusLabel.textProperty().unbind();
        progressBarStatusLabel.setText("");
        searchLoansProgressBar.setProgress(0);
    }

    public static List<LoanModel> makeLoanModelList(List<LoanData> loanDataList) {
        List<LoanModel> tempLoanModelList = new ArrayList<>();
        for(LoanData loanData : loanDataList) {
            LoanModel loanModel = new LoanModel.LoanModelBuilder()
                    .id(loanData.getName())
                    .amount(loanData.getBaseAmount())
                    .endYaz(loanData.getFinishedYaz())
                    .startYaz(loanData.getStartedYaz())
                    .nextPaymentInYaz(loanData.getNextPaymentInYaz())
                    .finalAmount(loanData.getFinalAmount())
                    .status(loanData.getStatus())
                    .investorsAmount(loanData.getInvestorsAmount())
                    .amountToActive(loanData.getAmountToActive())
                    .deriskAmount(loanData.getDeriskAmount())
                    .missingCycles(loanData.getMissingCycles())
                    .build();

            tempLoanModelList.add(loanModel);
        }
        return tempLoanModelList;
    }

    public void updateData() {
       // updateLoansExpander();
        updateLoansData();
        updatePaymentLoansData();
        updateTransactions();
        updateNotifications();
        updateOwnedInvestments();
    }

    private void updateLoansExpander() {
        updateExpander(loanerLoansTable);
        updateExpander(lenderLoansTable);
        updateExpander(loansFoundTable);
        updateExpander(loansChosenTable);
        updateExpander(loanerLoansPTable);
    }

    private void updateExpander(TableView<LoanModel> tableView) {
        tableView.getColumns().clear();
        LoanTable.setDataTables(tableView);
    }


    private Set<String> getSelectedCategories() {

        ObservableList<String> checkedItems = categoriesComboBox.getCheckModel().getCheckedItems();

        return checkedItems.stream().collect(Collectors.toSet());
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
            Platform.runLater(() -> {
                categoriesComboBox.getItems().setAll(categoriesList);
                categoriesComboBox.getCheckModel().checkAll();
            });
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

            tempLoanerModelList = makeLoanModelList(loanerDataList);
            tempLenderModelList = makeLoanModelList(lenderDataList);

            lenderModelList = tempLenderModelList;
            loanerModelList = tempLoanerModelList;
            requestedLoansAmountProperty.set(loanerModelList.size());

            Platform.runLater(() -> {
                loanerLoansTable.setItems(getLoans(loanerModelList));
                lenderLoansTable.setItems(getLoans(lenderModelList));
            });
        });

        updateThread.start();
    }

    public void updatePaymentLoansData() {
        Thread updatePaymentLoanThread = new Thread(() -> {
            List<LoanModel> tempLoanerModelList = new ArrayList<>();
            List<LoanData>  loanerDataList = null;
            try {
                loanerDataList = bankInstance.getUnFinishedLoans(customerId.get());
            } catch (DataNotFoundException e) {
                e.printStackTrace();
            }

            tempLoanerModelList = makeLoanModelList(loanerDataList);

            loanPModelList = tempLoanerModelList;
            paymentsAmountProperty.set(loanPModelList.size());

            Platform.runLater(() -> {
                loanerLoansPTable.setItems(getLoans(loanPModelList));
            });
        });

        updatePaymentLoanThread.start();
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

    private ObservableList<NotificationModel> getNotiications() {
        return FXCollections.observableArrayList(notificationModels);
    }

    private ObservableList<LoanModel> getLoans() {
        return FXCollections.observableArrayList();
    }

    private void updateTransactions() {
        if(!isFileSelected.get())
            return;
        Thread updateTransactions = new Thread(() -> {
            try {
                int balance = bankInstance.getCustomerDTO(customerId.get()).getAccount().getBalance();
                balanceProperty.set(balance);
                List<TransactionData> transactionsData = bankInstance.getTransactionsData(customerId.get()).getTransactions();
                List<TransactionModel> tempTransactionModels = new ArrayList<>();
                for (TransactionData data : transactionsData) {
                    tempTransactionModels.add(new TransactionModel.TransactionModelBuilder()
                            .description(data.getDescription())
                            .amount(data.getAmount())
                            .previousBalance(data.getPreviousBalance())
                            .yazMade(data.getYazMade())
                            .build());
                }
                transactionModels = tempTransactionModels;
            }
            catch(Exception e) {}
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
        TableColumn<NotificationModel, Integer> yazMadeColumn = new TableColumn<>("Date");
        yazMadeColumn.setCellValueFactory(new PropertyValueFactory<>("yazMade"));

        yazMadeColumn.maxWidthProperty().bind(notificationsTable.widthProperty().multiply(0.1));
        yazMadeColumn.setStyle("-fx-alignment: CENTER;");
        notificationMessageColumn.maxWidthProperty().bind(notificationsTable.widthProperty().multiply(0.9));
        notificationsTable.getColumns().addAll(notificationMessageColumn, yazMadeColumn);
    }

    private void updateNotifications() {
        if(!isFileSelected.get())
            return;
        Thread updateNotifications = new Thread(() -> {
            List<NotificationData> notificationsData = null;
            try {
                notificationsData = bankInstance.getNotificationsData(customerId.get()).getNotificationsList();
            } catch (DataNotFoundException e) {
                e.printStackTrace();
            }
            List<NotificationModel> tempNotificationModels = new ArrayList<>();
            for(NotificationData data : notificationsData) {
                tempNotificationModels.add(new NotificationModel.NotificationModelBuilder()
                        .message(data.getMessage())
                        .yazMade(data.getYazMade())
                        .build());
            }
            notificationModels = tempNotificationModels;
            Platform.runLater(() -> notificationsTable.setItems(getNotiications()));
        });
        updateNotifications.start();
    }

    public void createWithdraw(int amount) {
        Thread withdrawThread = new Thread(() -> {
            try {
                bankInstance.withdraw(customerId.get(), amount,"Withdraw");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        withdrawThread.start();
    }

    public void createDeposit(int amount) {
        Thread depositThread = new Thread(() -> {
            try {
                bankInstance.deposit(customerId.get(), amount,"Deposit");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        depositThread.start();
    }

    public void makeEnterAmountScreen(String message) {
        Stage newStage = new Stage();
        VBox comp = new VBox();
        Label messageLabel = new Label(message);
        TextField amountField = new TextField("Amount");
        comp.getChildren().add(messageLabel);
        comp.getChildren().add(amountField);

        HBox buttonsBox = new HBox();
        Button cancelButton = new Button("Cancel");
        Button enterButton = new Button("Submit");

        buttonsBox.getChildren().add(cancelButton);
        buttonsBox.getChildren().add(enterButton);

        comp.getChildren().add(buttonsBox);

        // Style
        comp.setAlignment(Pos.CENTER);
        comp.setFillWidth(true);
        newStage.initStyle(StageStyle.UNDECORATED);

        Scene stageScene = new Scene(comp, 300, 300);
        newStage.setScene(stageScene);
        newStage.show();
    }


    private List<InvestmentModel> getInvModels(InvestmentsSellData investmentsForSell) {
        List<InvestmentModel> investmentModels = new ArrayList<>();

        List<String> investorsIds = investmentsForSell.getInvestorsIds();
        List<String> investmentIds = investmentsForSell.getInvIds();
        List<String> loansIds = investmentsForSell.getLoansIds();
        List<Integer> amounts = investmentsForSell.getAmounts();
        List<Integer> yazPlaced = investmentsForSell.getYazPlaced();
        List<Boolean> forSale = investmentsForSell.getForSale();

        int arrSize = loansIds.size();

        for(int i = 0; i < arrSize; i++) {
            investmentModels.add(new InvestmentModel.InvestmentModelBuilder()
                    .loan(loansIds.get(i))
                    .owner(investorsIds.get(i))
                    .amount(amounts.get(i))
                    .yaz(yazPlaced.get(i))
                    .id(investmentIds.get(i))
                    .forSale(forSale.get(i))
                    .build());
        }
        return investmentModels;
    }

    private void updateOwnedInvestments() {
        Thread updateOwnedInvestments = new Thread(() -> {
            try {
                InvestmentsSellData customerInvestments = bankInstance.getCustomerInvestments(customerId.get());
                sellInvestmentModels = getInvModels(customerInvestments);


                Platform.runLater(() -> {
                    sellInvestmentTable.setItems(getSellInvestments());
                });

            } catch (DataNotFoundException e) {
                sellErrorLabel.setText(e.getMessage());
            }
        });
        updateOwnedInvestments.start();
    }

    private ObservableList<InvestmentModel> getSellInvestments() {
        return FXCollections.observableArrayList(sellInvestmentModels);
    }

}

