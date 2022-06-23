package client.screens.login;

import client.screens.admin.AdminController;
import http.constants.Constants;
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

    private final StringProperty currAdmin;
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
    private AdminController adminController;

    @FXML
    void logoutButtonAction(ActionEvent event) {
        if(!isConnected.get())
            return;

        new Thread(() -> {
            try {
                Response response = makeDisconnectRequest();
                if(response.isSuccessful()) {
                    Platform.runLater(() -> setDisconnected());
                    borderPane.setCenter(null);
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
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url(Constants.URL_LOGOUT)
                .build();
        return client.newCall(request).execute();
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
                    Response response = makeAdminLoginRequest(username);
                    if(response.isSuccessful()) {
                        currAdmin.set(username);
                        setAdminPage();

                        Platform.runLater(() -> {
                            setConnected();
                            setSuccessMessage("Logged In Successfully");
                        });
                    }
                    else {
                        String errorMessage = "";
                        if(response.body() != null)
                            errorMessage = response.body().string();

                        String finalErrorMessage = errorMessage;
                        Platform.runLater(() -> setErrorMessage(finalErrorMessage));
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
        currAdmin = new SimpleStringProperty();
    }

    public String getCurrAdmin() {
        return currAdmin.get();
    }

    public StringProperty currAdminProperty() {
        return currAdmin;
    }

    public boolean isIsConnected() {
        return isConnected.get();
    }

    public BooleanProperty isConnectedProperty() {
        return isConnected;
    }

    private void setAdminPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        // load admin fxml
        URL adminFXML = getClass().getResource(BankScreenConsts.ADMIN_FXML_RESOURCE_IDENTIFIER);
        loader.setLocation(adminFXML);
        Parent root = loader.load();


        adminController = loader.getController();
        Platform.runLater(() -> borderPane.setCenter(root));
    }

    private Response makeAdminLoginRequest(String username) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(Constants.URL_LOGIN_ADMIN + "?username=" + username)
                .build();
        return client.newCall(request).execute();
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
