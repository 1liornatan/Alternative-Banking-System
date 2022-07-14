package client.screens.admin;

import com.sun.istack.internal.NotNull;
import http.constants.Constants;
import http.utils.HttpClientUtil;
import http.utils.Props;
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
import manager.customers.CustomerData;
import manager.customers.CustomersData;
import manager.customers.CustomersWithVersion;
import manager.investments.PaymentsData;
import manager.loans.LoanData;
import manager.loans.LoansData;
import manager.loans.LoansWithVersion;
import manager.time.TimeData;
import models.CustomerModel;
import models.LoanModel;
import models.LoanStatusModel;
import models.utils.LoanTable;
import okhttp3.*;
import org.controlsfx.control.table.TableRowExpanderColumn;
import utils.ModelUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminController {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    final IntegerProperty currYazProperty;

    private List<CustomerModel> customerModelList;
    private List<LoanModel> loanModelList;
    private int loansVer, customersVer;

    @FXML
    private Button increaseYazButton;

    @FXML
    private Button customersButton;

    @FXML
    private TextField currYazTextField;

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
    private TextField rewindTextField;

    @FXML
    private Button startRewindButton;

    @FXML
    private Button stopRewindButton;


    private StringProperty loginErrorMessageProperty;
    private BooleanProperty isLoggedIn;
    private Thread updateThread;



    @FXML
    void logoutButtonYaz(ActionEvent event) {
        HttpClientUtil.runAsync(Constants.URL_LOGOUT, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Logout request ended with failure...:(");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful() || response.isRedirect()) {
                    HttpClientUtil.removeCookiesOf(Constants.URL_BASE);
                    Platform.runLater(() -> {
                        isLoggedIn.set(false);
                    });
                }
            }
        });
    }

    @FXML
    void customersButtonAction(ActionEvent event) {
        makeForecastRequest(Constants.ALL_CUSTOMERS);
    }

    private void makeForecastRequest(String type) {
        new Thread(() -> {
            try {
                String finalUrl = HttpUrl
                        .parse(Constants.URL_FORECAST)
                        .newBuilder()
                        .addQueryParameter(Constants.TYPE, type)
                        .build()
                        .toString();

                Request request = new Request.Builder()
                        .url(finalUrl)
                        .build();

                Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute();

                try(ResponseBody body = response.body()) {
                    String responseBody = body.string();
                    if (response.code() != 200) {
                        Platform.runLater(() ->
                                System.out.println("Something went wrong: " + responseBody)
                        );
                    } else {

                        PaymentsData data = Constants.GSON_INSTANCE.fromJson(responseBody, PaymentsData.class);

                        Platform.runLater(() -> {
                            updateCharts(data);
                        });
                    }
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }).start();
    }

    private void updateCharts(PaymentsData data) {
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
        makeForecastRequest(Constants.ALL_TRANSACTIONS);
    }

    private void updateCharts2(PaymentsData data) {
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
        makeForecastRequest(Constants.ALL_LOANS);
    }

    private void updateCharts3(PaymentsData data) {
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
    void startRewindButtonAction(ActionEvent event) {
        new Thread(() -> {
            try {
                Props props = new Props();
                props.add(Constants.TYPE, Constants.REWIND);
                props.add(Constants.AMOUNT, rewindTextField.getText());

                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, props.toString());
                Request request = new Request.Builder()
                        .url(Constants.URL_REWIND)
                        .method("POST", body)
                        .build();
                Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute();
                response.close();

                Platform.runLater(() -> {
                    startRewindButton.setDisable(true);
                });
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    @FXML
    void stopRewindButtonAction(ActionEvent event) {
        new Thread(() -> {
            try {
                Props props = new Props();
                props.add(Constants.TYPE, Constants.RESET);

                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, props.toString());
                Request request = new Request.Builder()
                        .url(Constants.URL_REWIND)
                        .method("POST", body)
                        .build();
                Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute();
                response.close();

                Platform.runLater(() -> {
                    startRewindButton.setDisable(false);
                });
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    @FXML
    void increaseYazButtonAction(ActionEvent event) {
        Thread increaseYazThread = new Thread(() -> {
            try {
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url(Constants.URL_TIME)
                        .method("POST", body)
                        .build();
                Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute();
                response.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        increaseYazThread.start();
    }

    public void updateBankData() {
        updateCustomersData();
        updateLoansData();
        updateTimeData();
    }

    private void updateTimeData() {
        new Thread(() -> {
            try {
                Request request = new Request.Builder()
                        .url(Constants.URL_TIME)
                        .build();
                Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute();

                if(response.isSuccessful()) {
                    try (ResponseBody responseBody = response.body()) {
                        String jsonResponse = responseBody.string();
                        TimeData data = Constants.GSON_INSTANCE.fromJson(jsonResponse, TimeData.class);
                        currYazProperty.set(data.getTime());
                    }
                }
                response.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    private void updateLoansData() {
        if(!isLoggedIn.get())
            return;

        new Thread(() -> {

            try {
                String finalUrl = HttpUrl
                        .parse(Constants.URL_LOAN)
                        .newBuilder()
                        .addQueryParameter(Constants.TYPE, Constants.ALL_LOANS)
                        .build()
                        .toString();

                Request request = new Request.Builder()
                        .url(finalUrl)
                        .build();
                Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute();

                try (ResponseBody body = response.body()) {
                    String responseBody = body.string();
                    if (response.code() != 200) {
                        Platform.runLater(() ->
                                System.out.println("Something went wrong: " + responseBody)
                        );
                    } else {
                        LoansWithVersion loansWithVersion = Constants.GSON_INSTANCE.fromJson(responseBody, LoansWithVersion.class);
                        LoansData loansData = loansWithVersion.getData();
                        List<LoanData> loanDataList = loansData.getLoans();

                        int ver = loansWithVersion.getLoansVer();

                        if(loansVer != ver) {
                            loansVer = ver;
                            loanModelList = ModelUtils.makeLoanModelList(loanDataList);

                            Platform.runLater(() -> {
                                Platform.runLater(() -> updateLoansTable());
                            });
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }).start();
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
        loginErrorMessageProperty = new SimpleStringProperty();
        currYazProperty = new SimpleIntegerProperty();
        customerModelList = new ArrayList<>();
        loanModelList = new ArrayList<>();
        isLoggedIn = new SimpleBooleanProperty(false);
        setUpdateThread();
        customersVer = 0;
        loansVer = 0;
    }

    public void startUpdateThread() {
        updateThread.start();
    }

    public void stopUpdateThread() {
        updateThread.interrupt();
    }
    @FXML
    void initialize() {
        setDataTables();
        increaseYazButton.setText("Increase\nYaz");
        increaseYazButton.setStyle("-fx-text-alignment: CENTER;");
        currYazTextField.textProperty().bind(currYazProperty.asString());
        stopRewindButton.disableProperty().bind(startRewindButton.disableProperty().not());
        rewindTextField.setText("1");
        rewindTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            int currYaz = currYazProperty.get();
            if (!newValue.matches("\\d*")) {
                rewindTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            else if(newValue.isEmpty()) {
                rewindTextField.setText("1");
            }
            else if(Integer.parseInt(newValue) > currYaz) {
                rewindTextField.setText(String.valueOf(currYaz));
            }
        });
        increaseYazButton.disableProperty().bind(startRewindButton.disableProperty());
    }

    public boolean isIsLoggedIn() {
        return isLoggedIn.get();
    }

    public BooleanProperty isLoggedInProperty() {
        return isLoggedIn;
    }

    private void setUpdateThread() {
        updateThread = new Thread(() -> {
            while(true) {
                try {
                    updateBankData();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("check update: " + e.getMessage());
                }
            }
        });
    }

    public void updateCustomersData() {
        if (!isLoggedIn.get())
            return;

        String finalUrl = Constants.URL_CUSTOMERS;

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody body = response.body()) {
                    String responseBody = body.string();
                    if (response.code() != 200) {

                        Platform.runLater(() ->
                                System.out.println("Something went wrong: " + responseBody)
                        );
                    } else {
                        CustomersWithVersion customersWithVerData = Constants.GSON_INSTANCE.fromJson(responseBody, CustomersWithVersion.class);
                        CustomersData customersData = customersWithVerData.getData();

                        int ver = customersWithVerData.getCustomersVer();
                        if(customersVer != ver) {
                            customersVer = ver;
                            List<CustomerData> customerDataList = customersData.getCustomers();
                            List<CustomerModel> tempCustomerModelList = ModelUtils.makeCustomerModelList(customerDataList);
                            customerModelList = tempCustomerModelList;
                            Platform.runLater(() -> updateCustomersTable());
                        }
                    }
                }
            }
        });


    }

    private void updateCustomersTable() {
        TableRowExpanderColumn<CustomerModel> customerModelTableColumn = (TableRowExpanderColumn<CustomerModel>) adminsCustomersTable.getColumns().get(3);
        ObservableList<CustomerModel> items = adminsCustomersTable.getItems();
        Set<Integer> expanded = new HashSet<>();

        int size = items.size();
        ObservableList<CustomerModel> customers = getCustomers();

        for (int i = 0; i < size; i++) {
            if (customerModelTableColumn.getExpandedProperty(items.get(i)).get())
                expanded.add(i);
        }

        adminsCustomersTable.setItems(customers);

        for (Integer index : expanded) {
            customerModelTableColumn.toggleExpanded(index);
        }
    }

    private void updateLoansTable() {

        TableRowExpanderColumn<LoanModel> loanModelTableExpander = (TableRowExpanderColumn<LoanModel>) adminLoansTable.getColumns().get(3);
        ObservableList<LoanModel> items = adminLoansTable.getItems();
        Set<Integer> expanded = new HashSet<>();

        int size = items.size();
        ObservableList<LoanModel> loans = getLoans();

        for (int i = 0; i < size; i++) {
            if (loanModelTableExpander.getExpandedProperty(items.get(i)).get())
                expanded.add(i);
        }

        adminLoansTable.setItems(loans);

        for (Integer index : expanded) {
            loanModelTableExpander.toggleExpanded(index);
        }
    }
}
