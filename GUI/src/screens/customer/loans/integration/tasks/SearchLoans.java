package screens.customer.loans.integration.tasks;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TableView;
import manager.investments.RequestDTO;
import manager.loans.LoansData;
import models.LoanModel;
import models.utils.ModelListUtils;
import screens.customer.loans.integration.IntegrationsReqs;

public class SearchLoans implements Runnable {
    private final IntegrationsReqs reqs;
    private final TableView<LoanModel> dataTable;
    private final String customerId;
    private DoubleProperty progress;
    private BooleanProperty inSearch;

    public SearchLoans(IntegrationsReqs reqs, TableView<LoanModel> dataTable, String customerId) {
        this.reqs = reqs;
        this.dataTable = dataTable;
        this.customerId = customerId;
        progress = new SimpleDoubleProperty(0);
        inSearch = new SimpleBooleanProperty(false);
    }

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    @Override
    public void run() {
        inSearch.set(true);
        RequestDTO requestDTO = new RequestDTO
                .Builder(customerId, reqs.getAmount())
                .categories(reqs.getCategories()) // TODO: apply optional options
                .minInterest(reqs.getMinInterest())
                .minDuration(reqs.getMinYaz())
                .maxLoans(reqs.getMaxLoans())
                .maxOwnership(reqs.getMaxOwnership())
                .build();

        try {
            Thread.sleep(2000);
            progress.set(0.3);
            LoansData loansData = bankInstance.loanAssignmentRequest(requestDTO);
            Thread.sleep(1000);
            progress.set(0.7);
            Thread.sleep(1000);
            progress.set(1.0);
            Platform.runLater(() -> {
                dataTable.getItems().setAll(ModelListUtils.makeLoanModelList(loansData.getLoans()));
            });
            inSearch.set(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isInSearch() {
        return inSearch.get();
    }

    public BooleanProperty inSearchProperty() {
        return inSearch;
    }
}
