package client.screens.login;

import client.screens.customer.CustomerController;
import http.constants.Constants;
import http.utils.HttpClientUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import okhttp3.*;
import screens.resources.BankScreenConsts;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class LoginController {

    private final StringProperty currCustomer;
    private final BooleanProperty isConnected;
    private Parent mainScreen;
    private Stage primaryStage;
    private BooleanProperty shakeProperty;
    private int x, y;

    private static final String STYLE_DEFAULT = Objects.requireNonNull(LoginController.class.getResource("/screens/resources/mainStyle.css")).toString();
    private static final String STYLE_DISCOUNT = Objects.requireNonNull(LoginController.class.getResource("/screens/resources/discount/styleDiscount.css")).toString();
    private static final String STYLE_HAPOALIM = Objects.requireNonNull(LoginController.class.getResource("/screens/resources/hapoalim/styleHapoalim.css")).toString();
    private static final String STYLE_ISRAEL = Objects.requireNonNull(LoginController.class.getResource("/screens/resources/israel/styleIsrael.css")).toString();

    @FXML
    private BorderPane borderPane;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginErrorLabel;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label connectionLabel;

    @FXML
    private Button logoutButton;
    private CustomerController customerController;

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
    @FXML
    void logoutButtonAction(ActionEvent event) {
        if(!isConnected.get())
            return;

        new Thread(() -> {
            try {
                Response response = makeDisconnectRequest();
                if(response.isSuccessful()) {
                    HttpClientUtil.shutdown();
                    HttpClientUtil.removeCookiesOf(Constants.ADDRESS);
                    customerController.stopUpdateThread();
                    Platform.runLater(() -> {
                        setDisconnected();
                        setSuccessMessage("Logged out Successfully");
                        borderPane.setCenter(null);
                    });
                }
                else {
                    Platform.runLater(() -> setErrorMessage("Something went wrong."));
                }
                response.close();
            } catch (IOException e) {
                Platform.runLater(() -> setErrorMessage(e.getMessage()));
            }
        }).start();
    }

    @FXML
    void initialize() {
        setDisconnected();
    }

    private Response makeDisconnectRequest() throws IOException {
        Request request = new Request.Builder()
                .url(Constants.URL_LOGOUT)
                .build();
        return HttpClientUtil.HTTP_CLIENT.newCall(request).execute();
    }

    @FXML
    void loginButtonAction(ActionEvent event) {
        if(isConnected.get()) {
            setErrorMessage("You are already connected!");
            return;
        }

        String username = usernameField.getText();

        if(username == null || username.length() == 0) {
            setErrorMessage("Username cannot be empty!");
        } else {
            new Thread(() -> {
                try {
                    Response response = makeLoginRequest(username);
                    if(response.isSuccessful()) {
                        currCustomer.set(username);
                        setCustomerPage();

                        Platform.runLater(() -> {
                            setConnected();
                            setSuccessMessage("Logged In Successfully");
                        });
                    }
                    else {
                        String errorMessage = "";
                        try (ResponseBody body = response.body()) {
                            if (body != null) {
                                errorMessage = body.string();
                            }

                            String finalErrorMessage = errorMessage;
                            Platform.runLater(() -> setErrorMessage(finalErrorMessage));
                        }
                    }
                    response.close();
                } catch (IOException e) {
                    Platform.runLater(() -> setSuccessMessage(e.getMessage()));
                }
            }).start();
        }
    }

    private void setConnected() {
        isConnected.set(true);
        connectionLabel.setTextFill(Color.GREEN);
        connectionLabel.setText("Connected");
    }

    private void setDisconnected() {
        isConnected.set(false);
        connectionLabel.setText("Disconnected");
        connectionLabel.setTextFill(Color.RED);
    }

    public LoginController() {
        isConnected = new SimpleBooleanProperty(false);
        currCustomer = new SimpleStringProperty();
    }

    public String getCurrCustomer() {
        return currCustomer.get();
    }

    public StringProperty currCustomerProperty() {
        return currCustomer;
    }

    public boolean isIsConnected() {
        return isConnected.get();
    }

    public BooleanProperty isConnectedProperty() {
        return isConnected;
    }

    private void setCustomerPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        // load admin fxml
        URL customerFXML = getClass().getResource(BankScreenConsts.CUSTOMER_FXML_RESOURCE_IDENTIFIER);
        loader.setLocation(customerFXML);
        Parent root = loader.load();


        customerController = loader.getController();
        shakeProperty = customerController.shakePropertyProperty();
        setShake();
        customerController.customerIdProperty().set(currCustomer.get());
        customerController.startUpdateThread();

        Platform.runLater(() -> borderPane.setCenter(root));
    }

    private void setShake() {
        shakeProperty.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    shakeStage();
                    shakeProperty.set(false);
                }
            }
        });
    }

    private Response makeLoginRequest(String username) throws IOException {
        String finalUrl = HttpUrl
                .parse(Constants.URL_LOGIN)
                .newBuilder()
                .addQueryParameter(Constants.USERNAME, username)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        return HttpClientUtil.HTTP_CLIENT.newCall(request).execute();
    }

    private void setErrorMessage(String message) {
        loginErrorLabel.setText(message);
        loginErrorLabel.setTextFill(Color.RED);
    }

    private void setSuccessMessage(String message) {
        loginErrorLabel.setText(message);
        loginErrorLabel.setTextFill(Color.GREEN);
    }

    public Parent getMainScreen() {
        return mainScreen;
    }

    public void setMainScreen(Parent mainScreen) {
        this.mainScreen = mainScreen;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
