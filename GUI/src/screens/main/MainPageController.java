package screens.main;

import bank.accounts.impl.Customer;
import bank.impl.BankImpl;
import bank.impl.exceptions.DataNotFoundException;
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
        adminController.updateCustomersData();
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
