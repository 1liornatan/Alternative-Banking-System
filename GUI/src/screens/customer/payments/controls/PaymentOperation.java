package screens.customer.payments.controls;

import bank.logic.accounts.impl.exceptions.NoMoneyException;
import bank.logic.accounts.impl.exceptions.NonPositiveAmountException;
import bank.logic.impl.exceptions.DataNotFoundException;
import javafx.application.Platform;
import javafx.scene.paint.Color;

public class PaymentOperation implements Runnable{

    private final Operations operation;
    private final int amount;
    private final String loanId;

    public PaymentOperation(String loanId, Operations operation, int amount) {
        this.operation = operation;
        this.amount = amount;
        this.loanId = loanId;
    }

    @Override
    public void run() {
        switch(operation) {
            case DEBT:
                //bankInstance.closeLoan(selectedLoan.getId());
                break;
            case CLOSE:
                //bankInstance.closeLoan(selectedLoan.getId());
                break;
            case CYCLE:
                //bankInstance.closeLoan(selectedLoan.getId());
                break;
        }
    }
}
