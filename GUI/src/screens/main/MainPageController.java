package screens.main;

import bank.accounts.impl.Customer;
import bank.impl.BankImpl;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import manager.customers.CustomerData;
import manager.loans.LoanData;
import models.CustomerModel;
import models.LoanModel;
import org.controlsfx.control.table.TableRowExpanderColumn;
import screens.admin.AdminController;
import screens.customer.CustomerController;
import screens.resources.BankScreenConsts;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainPageController {

    private BooleanProperty isFileSelected;
    private BooleanProperty isAdminScreen;

    private BorderPane mainScreen;
    private AnchorPane adminScreen;
    private AnchorPane customerScreen;

    private AdminController adminController;
    private CustomerController customerController;

    private CustomerModel customerModel;
    private LoanModel loanModel;

    private BankImpl bankInstance;

    @FXML
    private ComboBox viewComboBox;

    @FXML
    private TextField filePathTextField;

    @FXML
    private TextField currYazTextField;

    @FXML
    private Button increaseYazButton;

    @FXML
    private Button loadFileButton;

    @FXML
    private TableView<LoanModel> adminLoansTable;

    @FXML
    private TableView<CustomerModel> adminsCustomersTable;
    private ArrayList<CustomerModel> customerModelList;

    @FXML
    void increaseYazButtonAction(ActionEvent event) {

    }

    @FXML
    void viewComboBoxAction(ActionEvent event) {

    }

    public MainPageController() throws IOException {
        isAdminScreen = new SimpleBooleanProperty();
        isFileSelected = new SimpleBooleanProperty();

        FXMLLoader loader = new FXMLLoader();
        FXMLLoader loader2 = new FXMLLoader();

        URL customerFXML = getClass().getResource(BankScreenConsts.CUSTOMER_FXML_RESOURCE_IDENTIFIER);
        URL adminFXML = getClass().getResource(BankScreenConsts.ADMIN_FXML_RESOURCE_IDENTIFIER);

        loader.setLocation(customerFXML);
        customerScreen = loader.load();
        customerController = loader.getController();

        loader2.setLocation(adminFXML);
        adminScreen = loader2.load();
        adminController = loader2.getController();

        bankInstance = new BankImpl();
    }

    @FXML
    void initialize() {
        filePathTextField.textProperty().bind(adminController.getFilePathProperty());
        isFileSelected.bind(adminController.getFileSelectedProperty());
        viewComboBox.setItems(FXCollections.observableArrayList(new String("Admin"), new String("Menash")));
        viewComboBox.getSelectionModel().selectFirst();
        viewComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {

            if(oldVal.equals("Admin")) {
                setCustomerScreen();
            }
            else if(oldVal.equals("Menash")) {
                setAdminScreen();
            }

            updateScreenData();
        });
        adminController.setBankInstance(bankInstance);
        customerController.setBankInstance(bankInstance);
        currYazTextField.textProperty().bind(adminController.getCurrYazProperty().asString());

        setDataTables();
    }

    private void setDataTables() {
        //TableRowExpanderColumn<LoanModel> loanExpanderColumn = new TableRowExpanderColumn<>(this::createLoanEditor);
        TableRowExpanderColumn<CustomerModel> customerExpanderColumn = new TableRowExpanderColumn<>(this::createCustomerEditor);

        TableColumn<CustomerModel, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<CustomerModel, Integer> balanceColumn = new TableColumn<>("Balance");
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

/*        TableColumn<Customer, Integer> investedColumn = new TableColumn<>("Email");
        investedColumn.setCellValueFactory(new PropertyValueFactory<>("numOfInvested"));*/

        adminsCustomersTable.getColumns().addAll(nameColumn, balanceColumn);

        adminsCustomersTable.setItems(getCustomers());

    }

    private GridPane createCustomerEditor(TableRowExpanderColumn.TableRowDataFeatures<CustomerModel> param) {
        GridPane editor = new GridPane();
        editor.setPadding(new Insets(10));
        editor.setHgap(10);
        editor.setVgap(5);

        CustomerModel customer = param.getValue();

        TextField nameField = new TextField(customer.getName());
        TextField balanceField = new TextField(String.valueOf(customer.getBalance()));

        editor.addRow(0, new Label("Name"), nameField);
        editor.addRow(1, new Label("Balance"), balanceField);

/*        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            customer.setName(nameField.getText());
            customer.setEmail(emailField.getText());
            param.toggleExpanded();
        });*/

        Button cancelButton = new Button("Shrink");
        cancelButton.setOnAction(event -> param.toggleExpanded());

        editor.addRow(3, cancelButton);

        return editor;
    }

    private ObservableList<CustomerModel> getCustomers() {
        return FXCollections.observableArrayList(customerModelList);
    }

    private void updateScreenData() {
        updateCustomersData();
    }

    private void updateCustomersData() {
        Platform.runLater(() -> {
            List<CustomerData> customerDataList = bankInstance.getCustomersData();
            customerModelList = new ArrayList<>();
            for(CustomerData customerData : customerDataList) {
                CustomerModel customerModel = new CustomerModel();

                customerModel.setName(customerData.getName());
                customerModel.setBalance(customerData.getBalance());
                customerModel.setNumOfActiveLoansInvested(customerData.getNumOfActiveLoansInvested());
                customerModel.setNumOfActiveLoansRequested(customerData.getNumOfActiveLoansRequested());

                customerModelList.add(customerModel);
            }


        });
    }

    public void setAdminScreen() {
        mainScreen.setCenter(adminScreen);
    }

    public void setCustomerScreen() {
        mainScreen.setCenter(customerScreen);
    }


    public void setMainScreen(BorderPane mainScreen) {
        this.mainScreen = mainScreen;
    }
}
