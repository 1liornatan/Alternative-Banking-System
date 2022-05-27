package screens.main;

import bank.impl.BankImpl;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import manager.customers.CustomersNames;
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
        viewComboBox.setItems(FXCollections.observableArrayList(new String("Admin")));
        viewComboBox.getSelectionModel().selectFirst();
        viewComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {

            if(oldVal == null || newVal == null)
                return;
            else if(newVal.equals("Admin")) {
                updateScreenData();
                setAdminScreen();
            }
            else {
                customerController.updateData();
                setCustomerScreen();
            }
        });
        adminController.setBankInstance(bankInstance);
        customerController.setBankInstance(bankInstance);
        currYazTextField.textProperty().bind(adminController.getCurrYazProperty().asString());
        adminController.getFilePathProperty().addListener((obs, oldVal, newVal) -> {
            Thread customersNamesThread = new Thread(() -> {
                CustomersNames names = bankInstance.getCustomersNames();
                List<String> namesList = new ArrayList<>();

                namesList.add("Admin");
                for(String name : names.getNames())
                    namesList.add(name);

                customerController.updateCategories();
                Platform.runLater(() -> {
                    viewComboBox.setItems(FXCollections.observableArrayList(namesList));
                });
            });
            customersNamesThread.start();
        });
        customerController.customerIdProperty().bind(viewComboBox.valueProperty());
        customerController.isFileSelectedProperty().bind(adminController.getFileSelectedProperty());
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
