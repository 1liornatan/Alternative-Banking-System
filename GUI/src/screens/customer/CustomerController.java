package screens.customer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class CustomerController {

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

}
