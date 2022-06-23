package client.screens;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import screens.resources.BankScreenConsts;

import java.net.URL;

public class AdminMain extends Application {

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) throws Exception {
        //CSSFX.start();

        FXMLLoader loader = new FXMLLoader();

        // load main fxml
        URL adminFXML = getClass().getResource(BankScreenConsts.LOGIN_FXML_RESOURCE_IDENTIFIER);
        loader.setLocation(adminFXML);
        Parent root = loader.load();


        // set stage
        primaryStage.setTitle("Alternative Banking System - Admin Client");
        Scene scene = new Scene(root, 1050, 600);
        scene.getStylesheets().add(getClass().getResource("/screens/resources/mainStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
