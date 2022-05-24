package screens.customer;

import bank.impl.BankImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class CustomerController {

    private BankImpl bankInstance;

    @FXML
    private TableView<?> loanerLoansTable;

    @FXML
    private TableView<?> lenderLoansTable;

    @FXML
    private TableView<?> transactionsTable;

    @FXML
    private Button chargeButton;

    @FXML
    private Button withdrawButton;

    @FXML
    private TableView<?> loanerLoansPTable;

    @FXML
    private TableView<?> paymentControls;

    @FXML
    private TableView<?> notificationsTable;

    public BankImpl getBankInstance() {
        return bankInstance;
    }

    public void setBankInstance(BankImpl bankInstance) {
        this.bankInstance = bankInstance;
    }
}
