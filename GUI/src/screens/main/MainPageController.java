package screens.main;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import screens.resources.BankScreenConsts;
import sun.plugin.javascript.navig.Anchor;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MainPageController {

    StringProperty filePathProperty;
    BooleanProperty isFileSelected;
    BooleanProperty isAdminScreen;

    private BorderPane mainScreen;
    private AnchorPane adminScreen;
    private AnchorPane customerScreen;

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
        filePathProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty();
        isAdminScreen = new SimpleBooleanProperty();

        FXMLLoader loader = new FXMLLoader();
        FXMLLoader loader2 = new FXMLLoader();

        // load main fxml
        URL customerFXML = getClass().getResource(BankScreenConsts.CUSTOMER_FXML_RESOURCE_IDENTIFIER);
        URL adminFXML = getClass().getResource(BankScreenConsts.ADMIN_FXML_RESOURCE_IDENTIFIER);

        loader.setLocation(customerFXML);
        customerScreen = loader.load();

        loader2.setLocation(adminFXML);
        adminScreen = loader2.load();

    }

    @FXML
    void initialize() {

        filePathTextField.textProperty().bind(filePathProperty);
        isFileSelected.set(false);
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
