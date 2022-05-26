package models.utils;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import models.LoanModel;
import org.controlsfx.control.table.TableRowExpanderColumn;

public class LoanTable {

    public static GridPane createLoanExpander(TableRowExpanderColumn.TableRowDataFeatures<LoanModel> param) {
        GridPane expander = new GridPane();
        expander.setPadding(new Insets(10));
        expander.setHgap(10);
        expander.setVgap(5);

        LoanModel loan = param.getValue();

        setFields(expander, loan);

        return expander;
    }

    private static void setFields(GridPane expander, LoanModel loan) {
        TextField startYazField = new TextField(String.valueOf(loan.getStartYaz()));
        TextField endYazField = new TextField(String.valueOf(loan.getEndYaz()));
        TextField nextPaymentInField = new TextField(String.valueOf(loan.getNextPaymentInYaz()));

        startYazField.setEditable(false);
        endYazField.setEditable(false);
        nextPaymentInField.setEditable(false);

        startYazField.prefColumnCountProperty().bind(startYazField.textProperty().length());
        endYazField.prefColumnCountProperty().bind(endYazField.textProperty().length());
        nextPaymentInField.prefColumnCountProperty().bind(endYazField.textProperty().length());

        String loanStatus = loan.getStatus();

        expander.addRow(0, new Label("Started Yaz"), startYazField);
        expander.addRow(0, new Label("Finished Yaz"), endYazField);
        expander.addRow(1, new Label("Next Payment in"), nextPaymentInField);

    }

    public static void setDataTables(TableView<LoanModel> dataTable) {
        TableRowExpanderColumn<LoanModel> loanExpanderColumn = new TableRowExpanderColumn<>(LoanTable::createLoanExpander);
        loanExpanderColumn.setText("Details");

        TableColumn<LoanModel, String> loanNameColumn = new TableColumn<>("Id");
        loanNameColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        loanNameColumn.setPrefWidth(300);
        TableColumn<LoanModel, Integer> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        dataTable.getColumns().addAll(loanNameColumn, amountColumn, loanExpanderColumn);
    }
}
