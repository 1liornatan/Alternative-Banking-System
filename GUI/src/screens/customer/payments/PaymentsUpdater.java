package screens.customer.payments;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import manager.loans.LoanData;
import models.LoanModel;
import models.utils.ModelListUtils;

import java.util.List;

public class PaymentsUpdater implements Runnable {

    private final String customerId;
    private final TableView<LoanModel> paymentsTable;

    public PaymentsUpdater(String customerId, TableView<LoanModel> paymentsTable) {
        this.customerId = customerId;
        this.paymentsTable = paymentsTable;
    }

    @Override
    public void run() {
        List<LoanModel> paymentsList;
        List<LoanData>  loanerDataList = null;

        //loanerDataList = bankInstance.getUnFinishedLoans(customerId.get());

        paymentsList = ModelListUtils.makeLoanModelList(loanerDataList);

        Platform.runLater(() -> paymentsTable.setItems(FXCollections.observableArrayList(paymentsList)));
    }
}
