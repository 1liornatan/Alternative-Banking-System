package screens;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import screens.main.MainPageController;
import screens.resources.BankScreenConsts;

import java.net.URL;

public class MainPage extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //CSSFX.start();

        FXMLLoader loader = new FXMLLoader();

        // load main fxml
        URL mainFXML = getClass().getResource(BankScreenConsts.MAIN_FXML_RESOURCE_IDENTIFIER);
        loader.setLocation(mainFXML);
        BorderPane root = loader.load();

        // wire up controller

        MainPageController mainPageController = loader.getController();
        mainPageController.setMainScreen(root);
        mainPageController.setAdminScreen();
        //BusinessLogic businessLogic = new BusinessLogic(histogramController);
       //mainPageController.setBusinessLogic(businessLogic);


        // set stage
        primaryStage.setTitle("Alternative Banking System");
        Scene scene = new Scene(root, 1050, 600);
        scene.getStylesheets().add(getClass().getResource("/screens/resources/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
