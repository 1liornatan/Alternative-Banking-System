package utils;

import manager.customers.CustomerData;
import manager.loans.LoanData;
import manager.transactions.TransactionData;
import manager.transactions.TransactionsData;
import models.CustomerModel;
import models.LoanModel;
import models.TransactionModel;

import java.util.ArrayList;
import java.util.List;

public class ModelUtils {
    public static List<LoanModel> makeLoanModelList(List<LoanData> loanDataList) {
        List<LoanModel> tempLoanModelList = new ArrayList<>();
        for(LoanData loanData : loanDataList) {
            LoanModel loanModel = new LoanModel.LoanModelBuilder()
                    .id(loanData.getName())
                    .amount(loanData.getBaseAmount())
                    .endYaz(loanData.getFinishedYaz())
                    .startYaz(loanData.getStartedYaz())
                    .nextPaymentInYaz(loanData.getNextPaymentInYaz())
                    .finalAmount(loanData.getFinalAmount())
                    .status(loanData.getStatus())
                    .investorsAmount(loanData.getInvestorsAmount())
                    .amountToActive(loanData.getAmountToActive())
                    .deriskAmount(loanData.getDeriskAmount())
                    .missingCycles(loanData.getMissingCycles())
                    .payment(loanData.getNextPaymentAmount())
                    .left(loanData.getCloseAmount())
                    .build();

            tempLoanModelList.add(loanModel);
        }
        return tempLoanModelList;
    }

    public static List<CustomerModel> makeCustomerModelList(List<CustomerData> customerDataList) {
        List<CustomerModel> tempCustomerModelList = new ArrayList<>();
        for(CustomerData customerData : customerDataList) {
            CustomerModel customerModel = new CustomerModel();

            customerModel.setName(customerData.getName());
            customerModel.setBalance(customerData.getBalance());
            customerModel.setNumOfActiveLoansInvested(customerData.getNumOfActiveLoansInvested());
            customerModel.setNumOfActiveLoansRequested(customerData.getNumOfActiveLoansRequested());
            customerModel.setNumOfPendingLoansInvested(customerData.getNumOfPendingLoansInvested());
            customerModel.setNumOfPendingLoansRequested(customerData.getNumOfPendingLoansRequested());
            customerModel.setNumOfNewLoansInvested(customerData.getNumOfNewLoansInvested());
            customerModel.setNumOfNewLoansRequested(customerData.getNumOfNewLoansRequested());
            customerModel.setNumOfRiskLoansInvested(customerData.getNumOfRiskLoansInvested());
            customerModel.setNumOfRiskLoansRequested(customerData.getNumOfRiskLoansRequested());
            customerModel.setNumOfFinishedLoansInvested(customerData.getNumOfFinishedLoansInvested());
            customerModel.setNumOfFinishedLoansRequested(customerData.getNumOfFinishedLoansRequested());

            tempCustomerModelList.add(customerModel);
        }
        return tempCustomerModelList;
    }

    public static List<TransactionModel> makeTransactionsModelList(TransactionsData transactionsData) {
        List<TransactionModel> tempTransactionModels = new ArrayList<>();

        for (TransactionData data : transactionsData.getTransactions()) {
            tempTransactionModels.add(new TransactionModel.TransactionModelBuilder()
                    .description(data.getDescription())
                    .amount(data.getAmount())
                    .previousBalance(data.getPreviousBalance())
                    .yazMade(data.getYazMade())
                    .build());
        }
        return tempTransactionModels;
    }
}
