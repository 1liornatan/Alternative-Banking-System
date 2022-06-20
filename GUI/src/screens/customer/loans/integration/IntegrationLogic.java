package screens.customer.loans.integration;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableView;
import models.LoanModel;
import screens.customer.constants.Constants;
import screens.customer.loans.integration.tasks.SearchLoans;

import java.util.List;

/*
In order to make a search -
first setupSearch(reqs)
second makeSearch()
 */

public class IntegrationLogic {
    private final TableView<LoanModel> dataTable;
    private final StringProperty customerId;
    private DoubleProperty progress;
    private SearchLoans searchLoans;

    public IntegrationLogic(StringProperty customerId, TableView<LoanModel> dataTable) {
        this.dataTable = dataTable;
        this.customerId = customerId;
    }

    public void setupSearch(IntegrationsReqs reqs) {
        searchLoans = new SearchLoans(reqs, dataTable, customerId.get());
        progress = searchLoans.progressProperty();
    }

    public void makeSearch() {
        if(searchLoans == null)
            return;

        new Thread(searchLoans, Constants.THREAD_INTEGRATION_SEARCH).start();
    }

    public void makeInvestment(List<LoanModel> data) {

    }

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }
}
