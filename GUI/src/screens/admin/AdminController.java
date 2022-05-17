package screens.admin;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;

public class AdminController {

    BooleanProperty isFileSelected;
    StringProperty filePathProperty;

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
    void loadFileButtonAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select words file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        filePathProperty.set(absolutePath);
        isFileSelected.set(true);
    }

    public AdminController() {
        isFileSelected = new SimpleBooleanProperty();
    }

    public boolean isIsFileSelected() {
        return isFileSelected.get();
    }

    public BooleanProperty isFileSelectedProperty() {
        return isFileSelected;
    }

    public void setIsFileSelected(boolean isFileSelected) {
        this.isFileSelected.set(isFileSelected);
    }

    public String getFilePathProperty() {
        return filePathProperty.get();
    }

    public StringProperty filePathPropertyProperty() {
        return filePathProperty;
    }

    public void setFilePathProperty(String filePathProperty) {
        this.filePathProperty.set(filePathProperty);
    }


}
