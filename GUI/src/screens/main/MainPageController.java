package screens.main;

import bank.impl.BankImpl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import screens.admin.AdminController;
import screens.customer.CustomerController;
import screens.resources.BankScreenConsts;

import java.io.IOException;
import java.net.URL;

public class MainPageController {

    private BooleanProperty isFileSelected;
    private BooleanProperty isAdminScreen;

    private BorderPane mainScreen;
    private AnchorPane adminScreen;
    private AnchorPane customerScreen;

    private AdminController adminController;
    private CustomerController customerController;

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
    private TableView<?> adminLoansTable;

    @FXML
    private TableView<?> adminsCustomersTable;

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
    }

    private void updateScreenData() {

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
