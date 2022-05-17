package screens.admin;

import bank.impl.BankImpl;
import bank.impl.exceptions.DataNotFoundException;
import files.xmls.exceptions.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;

public class AdminController {

    BooleanProperty isFileSelected;
    StringProperty filePathProperty;
    IntegerProperty currYazProperty;
    private BankImpl bankInstance;

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

        try {
            bankInstance.loadData(absolutePath);
            filePathProperty.set(absolutePath);
            isFileSelected.set(true);
            updateBankData();
        } catch (NotXmlException | XmlNoLoanOwnerException | XmlNoCategoryException | XmlPaymentsException | XmlAccountExistsException | XmlNotFoundException | DataNotFoundException e) {
            System.out.println(e.getMessage());
            Alert errorMessage = new Alert(Alert.AlertType.ERROR, e.getMessage());
            errorMessage.show();
        }
    }

    private void updateBankData() {

    }

    public AdminController() {
        isFileSelected = new SimpleBooleanProperty();
        filePathProperty = new SimpleStringProperty();
        currYazProperty = new SimpleIntegerProperty();
    }

    public BooleanProperty getFileSelectedProperty() {
        return isFileSelected;
    }
    public StringProperty getFilePathProperty() {
        return filePathProperty;
    }
    public IntegerProperty getCurrYazProperty() { return currYazProperty; }

    @FXML
    void initialize() {
        isFileSelected.set(false);
    }

    public BankImpl getBankInstance() {
        return bankInstance;
    }

    public void setBankInstance(BankImpl bankInstance) {
        this.bankInstance = bankInstance;
    }

}
