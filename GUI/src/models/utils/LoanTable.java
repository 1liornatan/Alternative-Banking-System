package models.utils;

import bank.loans.LoanStatus;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
        LoanStatus loanStatus = LoanStatus.valueOf(loan.getStatus().toUpperCase());
        switch(loanStatus) {
            case ACTIVE:
                setActiveField(expander, loan);
                break;

            case FINISHED:
                setFinishedField(expander, loan);
                break;

            case PENDING:
                setPendingField(expander, loan);
                break;

            case RISKED:
                setRiskedField(expander, loan);
                break;

            case NEW:
                setNewField(expander, loan);
                break;
        }
    }

    private static void setNewField(GridPane expander, LoanModel loan) {
        TextField baseAmountField = new TextField(String.valueOf(loan.getAmount()));
        TextField fullAmountField = new TextField(String.valueOf(loan.getFinalAmount()));

        bindField(baseAmountField);
        bindField(fullAmountField);

        expander.addRow(0, new VBox(new Label("Base Amount"), baseAmountField));
        expander.addRow(0, new VBox(new Label("Final Amount"), fullAmountField));
    }

    private static void setRiskedField(GridPane expander, LoanModel loan) {
        TextField startYazField = new TextField(String.valueOf(loan.getStartYaz()));
        TextField nextPaymentField = new TextField(String.valueOf(loan.getNextPaymentInYaz()));
        TextField baseAmountField = new TextField(String.valueOf(loan.getAmount()));
        TextField deriskAmountField = new TextField(String.valueOf(loan.getDeriskAmount()));
        TextField missingCyclesField = new TextField(String.valueOf(loan.getMissingCycles()));
        TextField fullAmountField = new TextField(String.valueOf(loan.getFinalAmount()));

        bindField(startYazField);
        bindField(nextPaymentField);
        bindField(baseAmountField);
        bindField(fullAmountField);
        bindField(deriskAmountField);
        bindField(missingCyclesField);

        expander.addRow(0, new VBox(new Label("Started Yaz"), startYazField));
        expander.addRow(0, new VBox(new Label("Next Payment in"), nextPaymentField));
        expander.addRow(1, new VBox(new Label("Missing Cycles"), missingCyclesField));
        expander.addRow(1, new VBox(new Label("Debt Amount"), deriskAmountField));
        expander.addRow(2, new VBox(new Label("Base Amount"), baseAmountField));
        expander.addRow(2, new VBox(new Label("Final Amount"), fullAmountField));
    }
    private static void setPendingField(GridPane expander, LoanModel loan) {
        TextField baseAmountField = new TextField(String.valueOf(loan.getAmount()));
        TextField fullAmountField = new TextField(String.valueOf(loan.getFinalAmount()));
        TextField investorsField = new TextField(String.valueOf(loan.getInvestorsAmount()));
        TextField amountToActiveField = new TextField(String.valueOf(loan.getAmountToActive()));

        bindField(baseAmountField);
        bindField(fullAmountField);
        bindField(investorsField);
        bindField(amountToActiveField);

        expander.addRow(0, new VBox(new Label("Amount To Active"), amountToActiveField));
        expander.addRow(0, new VBox(new Label("Investors Amount"), investorsField));
        expander.addRow(1, new VBox(new Label("Base Amount"), baseAmountField));
        expander.addRow(1, new VBox(new Label("Final Amount"), fullAmountField));
    }

    private static void setFinishedField(GridPane expander, LoanModel loan) {
        TextField startYazField = new TextField(String.valueOf(loan.getStartYaz()));
        TextField endYazField = new TextField(String.valueOf(loan.getEndYaz()));
        TextField baseAmountField = new TextField(String.valueOf(loan.getAmount()));
        TextField fullAmountField = new TextField(String.valueOf(loan.getFinalAmount()));

        bindField(startYazField);
        bindField(endYazField);
        bindField(baseAmountField);
        bindField(fullAmountField);

        expander.addRow(0, new VBox(new Label("Started Yaz"), startYazField));
        expander.addRow(0, new VBox(new Label("Finished Yaz"), endYazField));
        expander.addRow(1, new VBox(new Label("Base Amount"), baseAmountField));
        expander.addRow(1, new VBox(new Label("Final Amount"), fullAmountField));
    }

    private static void setActiveField(GridPane expander, LoanModel loan) {
        TextField startYazField = new TextField(String.valueOf(loan.getStartYaz()));
        TextField nextPaymentField = new TextField(String.valueOf(loan.getNextPaymentInYaz()));
        TextField baseAmountField = new TextField(String.valueOf(loan.getAmount()));
        TextField fullAmountField = new TextField(String.valueOf(loan.getFinalAmount()));

        bindField(startYazField);
        bindField(nextPaymentField);
        bindField(baseAmountField);
        bindField(fullAmountField);

        expander.addRow(0, new VBox(new Label("Started Yaz"), startYazField));
        expander.addRow(0, new VBox(new Label("Next Payment in"), nextPaymentField));
        expander.addRow(1, new VBox(new Label("Base Amount"), baseAmountField));
        expander.addRow(1, new VBox(new Label("Final Amount"), fullAmountField));
    }

    public static void setDataTables(TableView<LoanModel> dataTable) {
        TableRowExpanderColumn<LoanModel> loanExpanderColumn = new TableRowExpanderColumn<>(LoanTable::createLoanExpander);
        loanExpanderColumn.setText("Details");

        TableColumn<LoanModel, String> loanNameColumn = new TableColumn<>("Id");
        loanNameColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<LoanModel, Integer> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<LoanModel, String> statusField = new TableColumn<>("Status");
        statusField.setCellValueFactory(new PropertyValueFactory<>("status"));

        loanNameColumn.minWidthProperty().bind(dataTable.widthProperty().multiply(0.5));
        amountColumn.minWidthProperty().bind(dataTable.widthProperty().multiply(0.2));
        statusField.minWidthProperty().bind(dataTable.widthProperty().multiply(0.2));
        loanExpanderColumn.minWidthProperty().bind(dataTable.widthProperty().multiply(0.1));

        amountColumn.setStyle("-fx-alignment: CENTER;");
        loanExpanderColumn.setStyle("-fx-alignment: TOP_CENTER;");
        statusField.setStyle("-fx-alignment: TOP_CENTER;");


        dataTable.getColumns().clear();
        dataTable.getColumns().addAll(loanNameColumn, amountColumn, statusField, loanExpanderColumn);
    }

    private static void bindField(TextField field) {
        field.prefColumnCountProperty().bind(field.textProperty().length());
        field.setEditable(false);
    }

}
