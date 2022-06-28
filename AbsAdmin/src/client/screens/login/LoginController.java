package client.screens.login;

import client.screens.admin.AdminController;
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
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class LoginController {

    private final StringProperty currAdmin;
    private final BooleanProperty isConnected;
    private Parent mainScreen;

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
    private AdminController adminController;

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
    void logoutButtonAction(ActionEvent event) {
        if(!isConnected.get())
            return;

        new Thread(() -> {
            try {
                Response response = makeDisconnectRequest();
                if(response.isSuccessful()) {
                    HttpClientUtil.shutdown();
                    HttpClientUtil.removeCookiesOf(Constants.ADDRESS);
                    Platform.runLater(() -> {
                                setDisconnected();
                                borderPane.setCenter(null);
                            });

                    adminController.isLoggedInProperty().set(false);
                    adminController.stopUpdateThread();

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
                        try (ResponseBody body = response.body()) {
                            if (body != null) {
                                errorMessage = body.string();
                            }
                        }
                        finally {
                            if (response != null && response.body() != null) {
                                response.body().close();
                            }
                        }

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
        adminController.isLoggedInProperty().set(true);
        adminController.startUpdateThread();

        Platform.runLater(() -> borderPane.setCenter(root));
    }

    private Response makeAdminLoginRequest(String username) throws IOException {
        String finalUrl = HttpUrl
                .parse(Constants.URL_LOGIN_ADMIN)
                .newBuilder()
                .addQueryParameter(Constants.USERNAME, username)
                .build()
                .toString();

        MediaType mediaType = MediaType.parse("text/plain");
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

}
