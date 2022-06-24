package client.screens.login;

import client.screens.customer.CustomerController;
import http.constants.Constants;
import http.utils.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import okhttp3.*;
import screens.resources.BankScreenConsts;

import java.io.IOException;
import java.net.URL;

public class LoginController {

    private final StringProperty currCustomer;
    private final BooleanProperty isConnected;

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
    void logoutButtonAction(ActionEvent event) {
        if(!isConnected.get())
            return;

        new Thread(() -> {
            try {
                Response response = makeDisconnectRequest();
                if(response.isSuccessful()) {
                    Platform.runLater(() -> {
                        setDisconnected();
                        borderPane.setCenter(null);
                    });
                }
                else {
                    Platform.runLater(() -> setErrorMessage("Something went wrong."));
                }
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
                            if (body != null)
                                errorMessage = body.string();

                            String finalErrorMessage = errorMessage;
                            Platform.runLater(() -> setErrorMessage(finalErrorMessage));
                        }
                    }
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
        customerController.customerIdProperty().set(currCustomer.get());
        customerController.updateData();

        Platform.runLater(() -> borderPane.setCenter(root));
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


}
