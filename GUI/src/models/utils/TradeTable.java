package models.utils;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import models.InvestmentModel;
import models.LoanModel;
import org.controlsfx.control.table.TableRowExpanderColumn;

public class TradeTable {

    public static void setDataTable(TableView<InvestmentModel> dataTable) {
        TableColumn<InvestmentModel, String> ownerNameColumn = new TableColumn<>("Investor Name");
        ownerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        TableColumn<InvestmentModel, String> loanNameColumn = new TableColumn<>("Loan Name");
        loanNameColumn.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        TableColumn<InvestmentModel, Integer> amountColumn = new TableColumn<>("Price");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<InvestmentModel, Integer> yazColumn = new TableColumn<>("Yaz Placed");
        yazColumn.setCellValueFactory(new PropertyValueFactory<>("yazMade"));

        loanNameColumn.minWidthProperty().bind(dataTable.widthProperty().multiply(0.4));
        ownerNameColumn.minWidthProperty().bind(dataTable.widthProperty().multiply(0.2));
        amountColumn.minWidthProperty().bind(dataTable.widthProperty().multiply(0.2));
        yazColumn.minWidthProperty().bind(dataTable.widthProperty().multiply(0.2));

        amountColumn.setStyle("-fx-alignment: CENTER;");
        yazColumn.setStyle("-fx-alignment: CENTER;");
        /*loanExpanderColumn.setStyle("-fx-alignment: TOP_CENTER;");*/


        dataTable.getColumns().addAll(loanNameColumn, ownerNameColumn, amountColumn, yazColumn);
    }
}
