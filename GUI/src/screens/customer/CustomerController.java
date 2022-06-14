package screens.customer;

import bank.logic.Bank;
import bank.logic.accounts.impl.exceptions.NoMoneyException;
import bank.logic.accounts.impl.exceptions.NonPositiveAmountException;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.loans.interest.exceptions.InvalidPercentException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import manager.investments.*;
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
import screens.MainPage;

import java.util.*;

public class CustomerController {

    private Bank bankInstance;
    private List<LoanModel> loanerModelList;
    private List<LoanModel> lenderModelList;
    private final StringProperty customerId;
    private final IntegerProperty investAmount;
    private final IntegerProperty balanceProperty;
    private final IntegerProperty minInterestProperty;
    private final IntegerProperty minLoanDurationProperty;
    private final IntegerProperty maxRequestedLoansProperty;
    private final IntegerProperty maxOwnershipFieldProperty;
    private ObservableList<String> categoriesList;
    private final BooleanProperty isFileSelected;
    private List<TransactionModel> transactionModels;
    private List<LoanModel> loanPModelList;
    private List<NotificationModel> notificationModels;
    private List<InvestmentModel> buyInvestmentModels;
    private List<InvestmentModel> sellInvestmentModels;
    private final IntegerProperty requestedLoansAmountProperty;
    private final IntegerProperty paymentsAmountProperty;
    private LoanModel selectedDebtLoan;
    private final BooleanProperty animationProperty;


    final static Image WALK_1 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/1.png")).toString());
    final static Image WALK_2 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/2.png")).toString());
    final static Image WALK_3 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/3.png")).toString());
    final static Image WALK_4 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/4.png")).toString());
    final static Image WALK_5 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/5.png")).toString());
    final static Image WALK_6 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/6.png")).toString());
    final static Image WALK_7 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/7.png")).toString());
    final static Image WALK_8 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/8.png")).toString());
    final static Image WALK_9 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/9.png")).toString());
    final static Image WALK_10 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/10.png")).toString());
    final static Image WALK_11 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/11.png")).toString());
    final static Image WALK_12 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/12.png")).toString());
    final static Image WALK_13 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/13.png")).toString());

    @FXML
    private ImageView animationImage;


    @FXML
    private Label infErrorLabel;


    @FXML
    private TextField balanceField;

    @FXML
    private TextField infAmountField;

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
    private ProgressBar searchLoansProgressBar;

    @FXML
    private Label progressBarStatusLabel;

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
    private HBox debtPaymentHBox;

    @FXML
    private TextField debtAmountField;

    @FXML
    private TableView<InvestmentModel> buyInvestmentTable;

    @FXML
    private Label buyErrorLabel;

    @FXML
    private Label paymentErrorLabel;

    @FXML
    private TableView<InvestmentModel> sellInvestmentTable;

    @FXML
    private Label sellErrorLabel;
    private final IntegerProperty debtAmountProperty;

    @FXML
    private LineChart<String, Number> timeLineChart;
    private Timeline walkTimeline;

    @FXML
    void buyInvestmentButtonAction(ActionEvent ignoredEvent) {
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
    void investButtonAction(ActionEvent ignoredEvent) {
        setInvestmentsChosen();
    }


    @FXML
    void listInvestmentButtonAction(ActionEvent ignoredEvent) {
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
    void searchInvestmentButtonAction(ActionEvent ignoredEvent) {
        Thread searchInvestmentsThread = new Thread(() -> {
            InvestmentsSellData investmentsForSell = bankInstance.getInvestmentsForSell(customerId.get());

            buyInvestmentModels = getInvModels(investmentsForSell);

            Platform.runLater(() -> buyInvestmentTable.setItems(getBuyInvestments()));

        });
        searchInvestmentsThread.start();
    }

    private ObservableList<InvestmentModel> getBuyInvestments() {
        return FXCollections.observableArrayList(buyInvestmentModels);
    }

    @FXML
    void unlistInvestmentButtonAction(ActionEvent ignoredEvent) {
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
    void tablesLeftButtonAction(ActionEvent ignoredEvent) {
        ObservableList<LoanModel> selectedItems = loansChosenTable.getSelectionModel().getSelectedItems();
        loansFoundTable.getItems().addAll(selectedItems);
        loansChosenTable.getItems().removeAll(selectedItems);
    }

    @FXML
    void withdrawButtonAction(ActionEvent ignoredEvent) {
        int amount = Integer.parseInt(infAmountField.getText());
        if(amount == 0) {
            infErrorLabel.setText("Amount must be higher than 0! ");
            infErrorLabel.setTextFill(Color.RED);
        }
        else {
            createWithdraw(amount);
            infAmountField.setText("0");
            updateData();
            infErrorLabel.setText("Successfully Withdrew " + amount);
            infErrorLabel.setTextFill(Color.GREEN);
        }
    }

    @FXML
    void chargeButtonAction(ActionEvent ignoredEvent) {
        int amount = Integer.parseInt(infAmountField.getText());
        if(amount == 0) {
            infErrorLabel.setText("Amount must be higher than 0! ");
            infErrorLabel.setTextFill(Color.RED);
        }
        else {
            createDeposit(amount);
            infAmountField.setText("0");
            updateData();
            infErrorLabel.setText("Successfully Deposited " + amount);
            infErrorLabel.setTextFill(Color.GREEN);
        }
    }

    @FXML
    void searchLoansButtonAction(ActionEvent ignoredEvent) {
        investAmount.set(Integer.parseInt(amountField.getText()));
        minInterestProperty.set(Integer.parseInt(minInterestField.getText()));
        minLoanDurationProperty.set(Integer.parseInt(minYazField.getText()));
        maxOwnershipFieldProperty.set(Integer.parseInt(maxOwnershipField.getText()));
        maxRequestedLoansProperty.set(Integer.parseInt(maxLoanerLoansField.getText()));
        searchLoansProgressBar.setProgress(0);

        if(investAmount.get() <= 0) {
            progressBarStatusLabel.textProperty().unbind();
            progressBarStatusLabel.setText("Amount must be positive!");
            progressBarStatusLabel.setTextFill(Color.RED);
            return;
        }

        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
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
                        .maxOwnership(maxOwnershipFieldProperty.get())
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
    void tablesRightButtonAction(ActionEvent ignoredEvent) {
        ObservableList<LoanModel> selectedItems = loansFoundTable.getSelectionModel().getSelectedItems();
        loansChosenTable.getItems().addAll(selectedItems);
        loansFoundTable.getItems().removeAll(selectedItems);
    }

    @FXML
    void closeLoanButtonAction(ActionEvent ignoredEvent) {
        LoanModel selectedLoan = loanerLoansPTable.getSelectionModel().getSelectedItem();
        if (selectedLoan == null) {
            paymentErrorLabel.setText("You must select a loan first!");
            paymentErrorLabel.setTextFill(Color.RED);
        }
        else {
            Thread closeLoanThread = new Thread(() -> {
                try {
                    bankInstance.closeLoan(selectedLoan.getId());
                    Platform.runLater(() -> {
                        updateData();
                        paymentErrorLabel.setText("Successfully Closed Loan!");
                        paymentErrorLabel.setTextFill(Color.GREEN);
                    });
                } catch (DataNotFoundException | NoMoneyException | NonPositiveAmountException e) {
                    Platform.runLater(() -> {
                        paymentErrorLabel.setText(e.getMessage());
                        paymentErrorLabel.setTextFill(Color.RED);
                    });
                }
            });
            closeLoanThread.start();
        }
    }

    @FXML
    void payCycleButtonAction(ActionEvent ignoredEvent) {
        LoanModel selectedLoan = loanerLoansPTable.getSelectionModel().getSelectedItem();
        if (selectedLoan == null) {
            paymentErrorLabel.setText("You must select a loan first!");
            paymentErrorLabel.setTextFill(Color.RED);
        }
        else {
            Thread payCycleThread = new Thread(() -> {
                try {
                    bankInstance.advanceCycle(selectedLoan.getId());
                    Platform.runLater(() -> {
                        updateData();
                        paymentErrorLabel.setText("Successfully paid " + selectedLoan.getPaymentAmount());
                        paymentErrorLabel.setTextFill(Color.GREEN);
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        paymentErrorLabel.setText(e.getMessage());
                        paymentErrorLabel.setTextFill(Color.RED);
                    });
                }

                });
            payCycleThread.start();
        }
    }

    @FXML
    void payDebtButtonAction(ActionEvent ignoredEvent) {
        selectedDebtLoan = loanerLoansPTable.getSelectionModel().getSelectedItem();

        if (selectedDebtLoan == null) {
            paymentErrorLabel.setText("You must select a loan first!");
            paymentErrorLabel.setTextFill(Color.RED);
            debtPaymentHBox.setDisable(true);
        }
        else {
            debtPaymentHBox.setDisable(false);
        }
    }

    @FXML
    void submitDebtButtonAction(ActionEvent ignoredEvent) {
        debtAmountProperty.set(Integer.parseInt(debtAmountField.getText()));
        Thread debtThread = new Thread(() -> {
            try {
                bankInstance.deriskLoanByAmount(selectedDebtLoan.getId(), debtAmountProperty.get());
                Platform.runLater(() -> {
                    updateData();
                    paymentErrorLabel.setText("Successfully paid " + debtAmountProperty.get());
                    paymentErrorLabel.setTextFill(Color.GREEN);
                });
            } catch (Exception e) {
                paymentErrorLabel.setText(e.getMessage());
                paymentErrorLabel.setTextFill(Color.RED);
            }
        });
        debtThread.start();
    }

    @FXML
    void initialize() {
        setDataTables();
        setSplitComps();
        setFieldLimits(amountField);
        setFieldLimits(infAmountField);
        minInterestField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                minInterestField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            else if(newValue.isEmpty() || Integer.parseInt(newValue) < 1) {
                minInterestField.setText("1");
            }
        });
        minYazField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                minYazField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            else if(newValue.isEmpty() || Integer.parseInt(newValue) < 1) {
                minYazField.setText("1");
            }
        });
        maxLoanerLoansField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                maxLoanerLoansField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            else if(newValue.isEmpty())
                maxLoanerLoansField.setText("0");
        });
        maxOwnershipField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                maxOwnershipField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            else if(newValue.isEmpty())
                maxOwnershipField.setText("0");
        });
        setFieldLimits(debtAmountField);

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
        setWalkAnimation();

    }

    private void setFieldLimits(TextField amountField) {
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                amountField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (newValue.isEmpty()) {
                amountField.setText("0");
            } else if (Integer.parseInt(newValue) > balanceProperty.get()) {
                amountField.setText(String.valueOf(balanceProperty.get()));
            }
        });
    }

    private void setWalkAnimation() {
        walkTimeline = new Timeline();
        walkTimeline.setCycleCount(Timeline.INDEFINITE);

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(100), (event) -> animationImage.setImage(WALK_1)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(200), (event) -> animationImage.setImage(WALK_2)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(300), (event) -> animationImage.setImage(WALK_3)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(400), (event) -> animationImage.setImage(WALK_4)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(500), (event) -> animationImage.setImage(WALK_5)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(600), (event) -> animationImage.setImage(WALK_6)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(700), (event) -> animationImage.setImage(WALK_7)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(800), (event) -> animationImage.setImage(WALK_8)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(900), (event) -> animationImage.setImage(WALK_9)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(1000), (event) -> animationImage.setImage(WALK_10)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(1100), (event) -> animationImage.setImage(WALK_11)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(1200), (event) -> animationImage.setImage(WALK_12)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(1300), (event) -> animationImage.setImage(WALK_13)));




        walkTimeline.play();
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
        sellInvestmentTable.getColumns().get(3).prefWidthProperty().bind(sellInvestmentTable.widthProperty().multiply(0.1));
        isListed.prefWidthProperty().bind(sellInvestmentTable.widthProperty().multiply(0.1));

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
        debtAmountProperty = new SimpleIntegerProperty();
        animationProperty = new SimpleBooleanProperty(true);
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
            loansChosenTable.getItems().forEach(loanModel -> selectedLoansIds.add(loanModel.getId()));
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
            } catch (Exception e) {
                progressBarStatusLabel.setText(e.getMessage());
                progressBarStatusLabel.setTextFill(Color.RED);
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
        clearAllFields();
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
                    .payment(loanData.getNextPaymentAmount())
                    .left(loanData.getCloseAmount())
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
        updateTimeChart();
    }



    private Set<String> getSelectedCategories() {

        ObservableList<String> checkedItems = categoriesComboBox.getCheckModel().getCheckedItems();

        return new HashSet<>(checkedItems);
    }

    public StringProperty customerIdProperty() {
        return customerId;
    }


    public void updateCategories() {
        if(!isFileSelected.get())
            return;

        Thread updateCategories = new Thread(() -> {
            Collection<String> categories = bankInstance.getCategoriesDTO().getCategories();
            ObservableList<String> tempCategoriesList = FXCollections.observableArrayList();
            tempCategoriesList.addAll(categories);
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
            List<LoanModel> tempLenderModelList;
            List<LoanModel> tempLoanerModelList;
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
            List<LoanModel> tempLoanerModelList;
            List<LoanData>  loanerDataList = null;
            try {
                loanerDataList = bankInstance.getUnFinishedLoans(customerId.get());
            } catch (DataNotFoundException e) {
                e.printStackTrace();
            }

            assert loanerDataList != null;
            tempLoanerModelList = makeLoanModelList(loanerDataList);

            loanPModelList = tempLoanerModelList;
            paymentsAmountProperty.set(loanPModelList.size());

            Platform.runLater(() -> loanerLoansPTable.setItems(getLoans(loanPModelList)));
        });

        updatePaymentLoanThread.start();
    }

    public void setBankInstance(Bank bankInstance) {
        this.bankInstance = bankInstance;
    }

    public BooleanProperty isFileSelectedProperty() {
        return isFileSelected;
    }

    private ObservableList<TransactionModel> getTransactions() {
        return FXCollections.observableArrayList(transactionModels);
    }

    private ObservableList<NotificationModel> getNotifications() {
        return FXCollections.observableArrayList(notificationModels);
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
            catch(Exception e) {
                paymentErrorLabel.setText(e.getMessage());
                paymentErrorLabel.setTextFill(Color.RED);
            }
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

        descriptionColumn.prefWidthProperty().bind(transactionsTable.widthProperty().multiply(0.5));
        balanceColumn.prefWidthProperty().bind(transactionsTable.widthProperty().multiply(0.2));
        previousBalanceColumn.prefWidthProperty().bind(transactionsTable.widthProperty().multiply(0.2));
        yazMadeColumn.prefWidthProperty().bind(transactionsTable.widthProperty().multiply(0.1));

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
        yazMadeColumn.setSortType(TableColumn.SortType.DESCENDING);
        notificationsTable.getSortOrder().clear();
        notificationsTable.getSortOrder().add(yazMadeColumn);

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
            assert notificationsData != null;
            for(NotificationData data : notificationsData) {
                tempNotificationModels.add(new NotificationModel.NotificationModelBuilder()
                        .message(data.getMessage())
                        .yazMade(data.getYazMade())
                        .build());
            }
            notificationModels = tempNotificationModels;
            Platform.runLater(() -> {
                notificationsTable.setItems(getNotifications());
                notificationsTable.sort();
            });
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


                Platform.runLater(() -> sellInvestmentTable.setItems(getSellInvestments()));

            } catch (DataNotFoundException e) {
                sellErrorLabel.setText(e.getMessage());
            }
        });
        updateOwnedInvestments.start();
    }

    private ObservableList<InvestmentModel> getSellInvestments() {
        return FXCollections.observableArrayList(sellInvestmentModels);
    }

    private void updateTimeChart() {
        int i;
        try {
            PaymentsData data = bankInstance.getPaymentsData(customerId.get());

            XYChart.Series series = new XYChart.Series();
            XYChart.Series forecasting = new XYChart.Series();
            series.setName("Payments Received");
            forecasting.setName("Forecasting");
            //populating the series with data

            List<Integer> payments = data.getPayments();
            List<Integer> amounts = data.getAmount();

            int yaz = payments.size();
            int sum = 0;

            for(i = 0; i < yaz; i++) {
                sum += amounts.get(i);
                series.getData().add(new XYChart.Data(String.valueOf(i+1), amounts.get(i)));
            }

            int avg = sum / (i+1);
            for(int j = 0; j < i + 5; j++) {
                forecasting.getData().add(new XYChart.Data(String.valueOf(j+1), avg*j));
            }

            timeLineChart.getData().clear();
            timeLineChart.getData().addAll(series, forecasting);
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public void animationOn() {
        walkTimeline.play();
        animationProperty.set(true);
        animationImage.setVisible(true);
    }

    public void animationOff() {
        walkTimeline.stop();
        animationProperty.set(false);
        animationImage.setVisible(false);
    }


}

