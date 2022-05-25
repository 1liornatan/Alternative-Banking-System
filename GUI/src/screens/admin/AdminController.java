package screens.admin;

import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.impl.BankImpl;
import bank.impl.exceptions.DataNotFoundException;
import files.xmls.exceptions.*;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import manager.customers.CustomerData;
import manager.loans.LoanData;
import models.CustomerModel;
import models.LoanModel;
import models.LoanStatusModel;
import models.utils.LoanTable;
import org.controlsfx.control.table.TableRowExpanderColumn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdminController {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    BooleanProperty isFileSelected;
    StringProperty filePathProperty;
    IntegerProperty currYazProperty;

    private BankImpl bankInstance;
    private List<CustomerModel> customerModelList;
    private List<LoanModel> loanModelList;

    @FXML
    private Button increaseYazButton;

    @FXML
    private Button loadFileButton;

    @FXML
    private TableView<LoanModel> adminLoansTable;

    @FXML
    private TableView<CustomerModel> adminsCustomersTable;

    @FXML
    void increaseYazButtonAction(ActionEvent event) {
        Thread increaseYazThread = new Thread(() -> {
            try {
                bankInstance.advanceOneYaz();
                updateBankData();
                int currYaz = bankInstance.getCurrentYaz();
                Platform.runLater(() -> currYazProperty.set(currYaz));
            } catch (DataNotFoundException | NonPositiveAmountException e) {
                e.printStackTrace();
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
            } catch (NotXmlException | XmlNoLoanOwnerException | XmlNoCategoryException | XmlPaymentsException | XmlAccountExistsException | XmlNotFoundException | DataNotFoundException e) {
                System.out.println(e.getMessage());
                Platform.runLater(() -> {
                    Alert errorMessage = new Alert(Alert.AlertType.ERROR, e.getMessage());
                    errorMessage.show();
                });
            }
        });
        loadBankThread.start();
    }

    private void updateBankData() {
        updateCustomersData();
        updateLoansData();
    }

    private void updateLoansData() {
        if(!isFileSelected.get())
            return;

        Thread updateLoansThread = new Thread(() -> {
            try {
                List<LoanModel> tempLoanModelList = new ArrayList<>();
                List<LoanData> loanDataList = bankInstance.getLoansData().getLoans();
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
                loanModelList = tempLoanModelList;
                Platform.runLater(() -> {
                    adminLoansTable.setItems(getLoans());
/*                    adminLoansTable.setRowFactory(tv -> {
                        TableRow<LoanModel> row = new TableRow<>();

                        row.setOnDragDetected(event -> {
                            if (! row.isEmpty()) {
                                Integer index = row.getIndex();
                                Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                                db.setDragView(row.snapshot(null, null));
                                ClipboardContent cc = new ClipboardContent();
                                cc.put(SERIALIZED_MIME_TYPE, index);
                                db.setContent(cc);
                                event.consume();
                            }
                        });

                        row.setOnDragOver(event -> {
                            Dragboard db = event.getDragboard();
                            if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                                if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                                    event.consume();
                                }
                            }
                        });
                        row.setOnDragDropped(event -> {
                            Dragboard db = event.getDragboard();
                            if(db.hasContent(SERIALIZED_MIME_TYPE)) {
                                int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                                LoanModel draggedLoan = adminLoansTable.getItems().remove(draggedIndex);
                                event.setDropCompleted(true);
                                event.consume();
                            }
                        });
                        return row;
                    });*/
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

/*        TextField nameField = new TextField(customer.getName());
        TextField balanceField = new TextField(String.valueOf(customer.getBalance()));

        nameField.setEditable(false);
        balanceField.setEditable(false);

        expander.addRow(0, new Label("Name"), nameField);
        expander.addRow(0, new Label("Balance"), balanceField);*/

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

    public BankImpl getBankInstance() {
        return bankInstance;
    }

    public void setBankInstance(BankImpl bankInstance) {
        this.bankInstance = bankInstance;
    }

}
