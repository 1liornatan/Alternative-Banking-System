package screens.customer.payments;

import bank.logic.accounts.impl.exceptions.NoMoneyException;
import bank.logic.accounts.impl.exceptions.NonPositiveAmountException;
import bank.logic.impl.exceptions.DataNotFoundException;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import models.LoanModel;
import screens.customer.constants.Constants;
import screens.customer.payments.controls.Operations;
import screens.customer.payments.controls.PaymentOperation;

public class PaymentsLogic {


    private final StringProperty customerId;
    private final TableView<LoanModel> paymentsTable;
    private final Label paymentErrorLabel;

    public PaymentsLogic(StringProperty customerId, TableView<LoanModel> paymentsTable, Label paymentErrorLabel) {
        this.customerId = customerId;
        this.paymentsTable = paymentsTable;
        this.paymentErrorLabel = paymentErrorLabel;
    }


    public void updatePayments() {
        new Thread(new PaymentsUpdater(customerId.get(), paymentsTable), Constants.THREAD_PAYMENTS).start();
    }

    public void makeOperation(Operations operation, int amount) {
        LoanModel selectedLoan = paymentsTable.getSelectionModel().getSelectedItem();
        if (selectedLoan == null) {
            paymentErrorLabel.setText("You must select a loan first!");
            paymentErrorLabel.setTextFill(Color.RED);
        }
        else {
            new Thread(new PaymentOperation(selectedLoan.getId(), operation, amount), Constants.THREAD_OPERATION).start();
        }
    }
}
