package screens.admin;

import bank.logic.Bank;
import bank.logic.impl.exceptions.DataNotFoundException;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import manager.customers.CustomerData;
import manager.investments.PaymentsData;
import manager.loans.LoanData;
import models.CustomerModel;
import models.LoanModel;
import models.LoanStatusModel;
import models.utils.LoanTable;
import models.utils.ModelListUtils;
import org.controlsfx.control.table.TableRowExpanderColumn;
import screens.customer.CustomerController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdminController {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    final BooleanProperty isFileSelected;
    final StringProperty filePathProperty;
    final IntegerProperty currYazProperty;

    private Bank bankInstance;
    private List<CustomerModel> customerModelList;
    private List<LoanModel> loanModelList;

    @FXML
    private Button increaseYazButton;

    @FXML
    private Button loadFileButton;

    @FXML
    private Button customersButton;

    @FXML
    private Button loansButton;

    @FXML
    private Button transactionsButton;

    @FXML
    private TableView<LoanModel> adminLoansTable;

    @FXML
    private TableView<CustomerModel> adminsCustomersTable;

    @FXML
    private LineChart<String, Integer> timeLineChart;

    @FXML
    void customersButtonAction(ActionEvent event) {
        PaymentsData data = bankInstance.getAllCustomersData();

        int i;
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        XYChart.Series<String, Integer> forecasting = new XYChart.Series<>();
        XYChart.Series<String, Integer> seriesA = new XYChart.Series<>();
        XYChart.Series<String, Integer> forecastingA = new XYChart.Series<>();
        seriesA.setName("Balance in ABS");
        forecasting.setName("Forecasting balance");
        series.setName("Customers in ABS");
        forecasting.setName("Forecasting customers");
        //populating the series with data

        List<Integer> payments = data.getPayments();
        List<Integer> amounts = data.getAmount();

        int yaz = payments.size();
        int sum = 0;
        int sumBalance = 0;

        for(i = 0; i < yaz; i++) {
            sum += payments.get(i);
            sumBalance += amounts.get(i);
            series.getData().add(new XYChart.Data<>(String.valueOf(i+1), payments.get(i)));
            seriesA.getData().add(new XYChart.Data<>(String.valueOf(i+1), amounts.get(i)));
        }

        int avg = sum / (i+1);
        int avgBalance = sumBalance / (i+1);
        for(int j = 0; j < i + 5; j++) {
            forecasting.getData().add(new XYChart.Data<>(String.valueOf(j+1), avg*j));
            forecastingA.getData().add(new XYChart.Data<>(String.valueOf(j+1), avg*(i+1)));
        }

        timeLineChart.getData().clear();
        timeLineChart.getData().addAll(series, forecasting/*, seriesA, forecastingA*/);
    }

    @FXML
    void transactionsButtonAction(ActionEvent event) {
        PaymentsData data = bankInstance.getAllTransactionsData();

        int i;
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        XYChart.Series<String, Integer> forecasting = new XYChart.Series<>();
        XYChart.Series<String, Integer> seriesA = new XYChart.Series<>();
        XYChart.Series<String, Integer> forecastingA = new XYChart.Series<>();        seriesA.setName("Transactions Amount");
        forecasting.setName("Forecasting #Transactions");
        series.setName("#Transactions");
        forecasting.setName("Forecasting Amount");
        //populating the series with data

        List<Integer> payments = data.getPayments();
        List<Integer> amounts = data.getAmount();

        int yaz = payments.size();
        int sum = 0;
        int sumBalance = 0;

        for(i = 0; i < yaz; i++) {
            sum += payments.get(i);
            sumBalance += amounts.get(i);
            series.getData().add(new XYChart.Data<>(String.valueOf(i+1), payments.get(i)));
            seriesA.getData().add(new XYChart.Data<>(String.valueOf(i+1), amounts.get(i)));
        }

        int avg = sum / (i+1);
        int avgBalance = sumBalance / (i+1);
        for(int j = 0; j < i + 5; j++) {
            forecasting.getData().add(new XYChart.Data<>(String.valueOf(j+1), avg*j));
            forecastingA.getData().add(new XYChart.Data<>(String.valueOf(j+1), avg*(i+1)));
        }

        timeLineChart.getData().clear();
        timeLineChart.getData().addAll(series, forecasting/*, seriesA, forecastingA*/);
    }

    @FXML
    void loansButtonAction(ActionEvent event) {
        PaymentsData data = bankInstance.getAllLoansData();

        int i = 0;
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        XYChart.Series<String, Integer> forecasting = new XYChart.Series<>();
        XYChart.Series<String, Integer> seriesA = new XYChart.Series<>();
        XYChart.Series<String, Integer> forecastingA = new XYChart.Series<>();
        seriesA.setName("Loans Amount");
        forecasting.setName("Forecasting Amount");
        series.setName("#Loans");
        forecasting.setName("Forecasting #Loans");
        //populating the series with data

        List<Integer> payments = data.getPayments();
        List<Integer> amounts = data.getAmount();

        int yaz = payments.size();
        int sum = 0;
        int sumBalance = 0;

        for(i = 0; i < yaz; i++) {
            sum += payments.get(i);
            sumBalance += amounts.get(i);
            series.getData().add(new XYChart.Data<>(String.valueOf(i+1), payments.get(i)));
            seriesA.getData().add(new XYChart.Data<>(String.valueOf(i+1), amounts.get(i)));
        }

        int avg = sum / (i+1);
        int avgBalance = sumBalance / (i+1);
        for(int j = 0; j < i + 5; j++) {
            forecasting.getData().add(new XYChart.Data<>(String.valueOf(j+1), avg*j));
            forecastingA.getData().add(new XYChart.Data<>(String.valueOf(j+1), avg*(i+1)));
        }

        timeLineChart.getData().clear();
        timeLineChart.getData().addAll(series, forecasting/*, seriesA, forecastingA*/);
    }

    @FXML
    void increaseYazButtonAction(ActionEvent event) {
        Thread increaseYazThread = new Thread(() -> {
            try {
                bankInstance.advanceOneYaz();
                updateBankData();
                int currYaz = bankInstance.getCurrentYaz();
                Platform.runLater(() -> currYazProperty.set(currYaz));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        increaseYazThread.start();
    }

    @FXML
    void loadFileButtonAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select words file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();

        Thread loadBankThread = new Thread(() -> {
            try {
                bankInstance.loadData(absolutePath);
                isFileSelected.set(true);
                updateBankData();
                int currYaz = bankInstance.getCurrentYaz();
                Platform.runLater(() -> {
                    filePathProperty.set(absolutePath);
                    currYazProperty.set(currYaz);
                });
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Platform.runLater(() -> {
                    Alert errorMessage = new Alert(Alert.AlertType.ERROR, e.getMessage());
                    errorMessage.show();
                });
            }
        });
        loadBankThread.start();
    }

    public void updateBankData() {
        updateCustomersData();
        updateLoansData();
    }

    private void updateLoansData() {
        if(!isFileSelected.get())
            return;

        Thread updateLoansThread = new Thread(() -> {
            try {
                List<LoanData> loanDataList = bankInstance.getLoansData().getLoans();
                loanModelList = ModelListUtils.makeLoanModelList(loanDataList);
                Platform.runLater(() -> {
                    adminLoansTable.setItems(getLoans());
                });
            } catch (DataNotFoundException e) {
                e.printStackTrace();
            }
        });

        updateLoansThread.start();
    }


    private GridPane createCustomerExpander(TableRowExpanderColumn.TableRowDataFeatures<CustomerModel> param) {
        GridPane expander = new GridPane();
        expander.setPadding(new Insets(10));
        expander.setHgap(10);
        expander.setVgap(5);

        CustomerModel customer = param.getValue();

        TableView<LoanStatusModel> expanderTable = new TableView<>();

        TableColumn<LoanStatusModel, String> nameColumn = new TableColumn<>("");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<LoanStatusModel, Integer> activeColumn = new TableColumn<>("Active");
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("numOfActiveLoansRequested"));

        TableColumn<LoanStatusModel, Integer> pendingColumn = new TableColumn<>("Pending");
        pendingColumn.setCellValueFactory(new PropertyValueFactory<>("numOfPendingLoansRequested"));

        TableColumn<LoanStatusModel, Integer> newColumn = new TableColumn<>("New");
        newColumn.setCellValueFactory(new PropertyValueFactory<>("numOfNewLoansRequested"));

        TableColumn<LoanStatusModel, Integer> riskColumn = new TableColumn<>("Risk");
        riskColumn.setCellValueFactory(new PropertyValueFactory<>("numOfRiskLoansRequested"));

        TableColumn<LoanStatusModel, Integer> finishedColumn = new TableColumn<>("Finished");
        finishedColumn.setCellValueFactory(new PropertyValueFactory<>("numOfFinishedLoansRequested"));

        expanderTable.getColumns().addAll(nameColumn, activeColumn, newColumn, pendingColumn, riskColumn, finishedColumn);

        expanderTable.setItems(FXCollections.observableArrayList(
                new LoanStatusModel("Requested", customer.getNumOfActiveLoansRequested(), customer.getNumOfPendingLoansRequested(),
                        customer.getNumOfNewLoansRequested(), customer.getNumOfRiskLoansRequested(), customer.getNumOfFinishedLoansRequested()),
                new LoanStatusModel("Invested", customer.getNumOfActiveLoansInvested(), customer.getNumOfPendingLoansInvested(),
                                customer.getNumOfNewLoansInvested(), customer.getNumOfRiskLoansInvested(), customer.getNumOfFinishedLoansInvested())));

/*        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            customer.setName(nameField.getText());
            customer.setEmail(emailField.getText());
            param.toggleExpanded();
        });*/
        expanderTable.setMinHeight(75);
        expanderTable.setMaxHeight(75);
        expander.setMinHeight(75);
        expander.setMaxHeight(75);
        expander.addRow(0, expanderTable);
        expander.getStylesheets().add(getClass().getResource("/screens/resources/adminStyle.css").toExternalForm());
        return expander;
    }

    private void setDataTables() {
        TableRowExpanderColumn<CustomerModel> customerExpanderColumn = new TableRowExpanderColumn<>(this::createCustomerExpander);
        customerExpanderColumn.setText("Loans");
        customerExpanderColumn.setStyle("-fx-alignment: TOP_CENTER;");

        TableColumn<CustomerModel, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<CustomerModel, Integer> balanceColumn = new TableColumn<>("Balance");
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));


        adminsCustomersTable.getColumns().addAll(nameColumn, balanceColumn, customerExpanderColumn);

        LoanTable.setDataTables(adminLoansTable);

        updateBankData();
        adminLoansTable.setItems(getLoans());
        adminsCustomersTable.setItems(getCustomers());

    }

    private ObservableList<LoanModel> getLoans() {
        return FXCollections.observableArrayList(loanModelList);
    }

    private ObservableList<CustomerModel> getCustomers() {
        return FXCollections.observableArrayList(customerModelList);
    }

    public AdminController() {
        isFileSelected = new SimpleBooleanProperty();
        filePathProperty = new SimpleStringProperty();
        currYazProperty = new SimpleIntegerProperty();
        customerModelList = new ArrayList<>();
        loanModelList = new ArrayList<>();
    }

    public BooleanProperty getFileSelectedProperty() {
        return isFileSelected;
    }
    public StringProperty getFilePathProperty() {
        return filePathProperty;
    }
    public IntegerProperty getCurrYazProperty() { return currYazProperty; }

    @FXML
    void initialize() {
        isFileSelected.set(false);
        setDataTables();
        increaseYazButton.disableProperty().bind(isFileSelected.not());
        adminsCustomersTable.disableProperty().bind(isFileSelected.not());
        adminLoansTable.disableProperty().bind(isFileSelected.not());
        loansButton.disableProperty().bind(isFileSelected.not());
        transactionsButton.disableProperty().bind(isFileSelected.not());
        customersButton.disableProperty().bind(isFileSelected.not());
        increaseYazButton.setText("Increase\nYaz");
        increaseYazButton.setStyle("-fx-text-alignment: CENTER;");
        loadFileButton.setText("Load\nFile");
        loadFileButton.setStyle("-fx-text-alignment: CENTER;");


    }

    public void updateCustomersData() {
        if(!isFileSelected.get())
            return;

        Thread updateThread = new Thread(() -> {
            try {
                List<CustomerModel> tempCustomerModelList = new ArrayList<>();
                List<CustomerData> customerDataList = bankInstance.getCustomersData().getCustomers();
                for(CustomerData customerData : customerDataList) {
                    CustomerModel customerModel = new CustomerModel();

                    customerModel.setName(customerData.getName());
                    customerModel.setBalance(customerData.getBalance());
                    customerModel.setNumOfActiveLoansInvested(customerData.getNumOfActiveLoansInvested());
                    customerModel.setNumOfActiveLoansRequested(customerData.getNumOfActiveLoansRequested());
                    customerModel.setNumOfPendingLoansInvested(customerData.getNumOfPendingLoansInvested());
                    customerModel.setNumOfPendingLoansRequested(customerData.getNumOfPendingLoansRequested());
                    customerModel.setNumOfNewLoansInvested(customerData.getNumOfNewLoansInvested());
                    customerModel.setNumOfNewLoansRequested(customerData.getNumOfNewLoansRequested());
                    customerModel.setNumOfRiskLoansInvested(customerData.getNumOfRiskLoansInvested());
                    customerModel.setNumOfRiskLoansRequested(customerData.getNumOfRiskLoansRequested());
                    customerModel.setNumOfFinishedLoansInvested(customerData.getNumOfFinishedLoansInvested());
                    customerModel.setNumOfFinishedLoansRequested(customerData.getNumOfFinishedLoansRequested());

                    tempCustomerModelList.add(customerModel);
                }
                customerModelList = tempCustomerModelList;
                Platform.runLater(() -> adminsCustomersTable.setItems(getCustomers()));
            } catch (DataNotFoundException e) {
                e.printStackTrace();
            }
        });

        updateThread.start();
    }

    public Bank getBankInstance() {
        return bankInstance;
    }

    public void setBankInstance(Bank bankInstance) {
        this.bankInstance = bankInstance;
    }

/*
    public ObservableList<XYChart.Data<Integer, Integer>> plot(int... y) {
        final ObservableList<XYChart.Data<Integer, Integer>> dataset = FXCollections.observableArrayList();
        int i = 0;
        while (i < y.length) {
            final XYChart.Data<Integer, Integer> data = new XYChart.Data<>(i + 1, y[i]);
            data.setNode(
                    new HoveredThresholdNode(
                            (i == 0) ? 0 : y[i-1],
                            y[i]
                    )
            );

            dataset.add(data);
            i++;
        }

        return dataset;
    }
*/

}
