package screens.main;

import bank.logic.Bank;
import bank.logic.impl.BankImpl;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import manager.customers.CustomersNames;
import screens.admin.AdminController;
import screens.customer.CustomerController;
import screens.resources.BankScreenConsts;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainPageController {

    private final BooleanProperty isFileSelected;

    private BorderPane mainScreen;
    private Stage primaryStage;
    private final Parent adminScreen;
    private final Parent customerScreen;

    private final AdminController adminController;
    private final CustomerController customerController;

    private int x, y;

    private final Bank bankInstance;

    private static final String STYLE_DEFAULT = Objects.requireNonNull(MainPageController.class.getResource("/screens/resources/mainStyle.css")).toString();
    private static final String STYLE_DISCOUNT = Objects.requireNonNull(MainPageController.class.getResource("/screens/resources/discount/styleDiscount.css")).toString();
    private static final String STYLE_HAPOALIM = Objects.requireNonNull(MainPageController.class.getResource("/screens/resources/hapoalim/styleHapoalim.css")).toString();
    private static final String STYLE_ISRAEL = Objects.requireNonNull(MainPageController.class.getResource("/screens/resources/israel/styleIsrael.css")).toString();

    @FXML
    private ComboBox<String> viewComboBox;

    @FXML
    private TextField filePathTextField;

    @FXML
    private TextField currYazTextField;

    @FXML
    private Button increaseYazButton;

    @FXML
    private Button loadFileButton;

    @FXML
    void styleDiscountAction(ActionEvent event) {
        mainScreen.getStylesheets().clear();
        mainScreen.getStylesheets().add(STYLE_DISCOUNT);
        mainScreen.applyCss();
    }

    @FXML
    void styleHapoalimAction(ActionEvent event) {
        mainScreen.getStylesheets().clear();
        mainScreen.getStylesheets().add(STYLE_HAPOALIM);
        mainScreen.applyCss();
    }

    @FXML
    void styleIsraelAction(ActionEvent event) {
        mainScreen.getStylesheets().clear();
        mainScreen.getStylesheets().add(STYLE_ISRAEL);
        mainScreen.applyCss();
    }

    @FXML
    void styleDefaultAction(ActionEvent event) {
        mainScreen.getStylesheets().clear();
        mainScreen.getStylesheets().add(STYLE_DEFAULT);
        mainScreen.applyCss();
    }


    @FXML
    void animationOffAction(ActionEvent event) {
        customerController.animationOff();
    }

    @FXML
    void animationOnAction(ActionEvent event) {
        customerController.animationOn();
    }

    @FXML
    void shakeScreenAction(ActionEvent event) {
        shakeStage();
    }

    public MainPageController() throws IOException {
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

        x = 0;
        y = 0;
    }


    @FXML
    void initialize() {
        filePathTextField.textProperty().bind(adminController.getFilePathProperty());
        isFileSelected.bind(adminController.getFileSelectedProperty());
        viewComboBox.setItems(FXCollections.observableArrayList("Admin"));
        viewComboBox.getSelectionModel().selectFirst();
        viewComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {

            if(oldVal == null || newVal == null)
                return;
            else if(newVal.equals("Admin")) {
                setAdminScreen();
                updateScreenData();

            }
            else {
                customerController.accountChanged();
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
                namesList.addAll(names.getNames());

                customerController.updateCategories();
                Platform.runLater(() -> viewComboBox.setItems(FXCollections.observableArrayList(namesList)));
            });
            customersNamesThread.start();
        });
        customerController.customerIdProperty().bind(viewComboBox.valueProperty());
        customerController.isFileSelectedProperty().bind(adminController.getFileSelectedProperty());
    }



    private void updateScreenData() {
        adminController.updateBankData();
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

    public void shakeStage() {
        Timeline timelineX = new Timeline(new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (x == 0) {
                    primaryStage.setX(primaryStage.getX() + 10);
                    x = 1;
                } else {
                    primaryStage.setX(primaryStage.getX() - 10);
                    x = 0;
                }
            }
        }));

        timelineX.setCycleCount(22);
        timelineX.setAutoReverse(false);
        timelineX.play();


        Timeline timelineY = new Timeline(new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (y == 0) {
                    primaryStage.setY(primaryStage.getY() + 10);
                    y = 1;
                } else {
                    primaryStage.setY(primaryStage.getY() - 10);
                    y = 0;
                }
            }
        }));

        timelineY.setCycleCount(22);
        timelineY.setAutoReverse(false);
        timelineY.play();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
