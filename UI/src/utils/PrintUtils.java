package utils;

import bank.impl.exceptions.DataNotFoundException;
import bank.loans.Loan;
import manager.customers.CustomerDTO;
import manager.customers.CustomersDTO;
import manager.loans.LoanDTO;
import manager.loans.LoansDTO;
import manager.transactions.TransactionDTO;
import manager.transactions.TransactionsDTO;


import java.util.ArrayList;
import java.util.List;

public class PrintUtils {

    public static void printCustomersNames(CustomersDTO customers) {
        List<CustomerDTO> customersList = customers.getCustomers();
        System.out.println("All customers names:");
        for(CustomerDTO customer : customersList) {
            System.out.println(customer.getName());
        }
    }

    public static void printCustomerName(CustomerDTO customer) {
            System.out.println(customer.getName());
    }

    public static void printTransactions(TransactionsDTO transactionsDTO) {
        List<TransactionDTO> transactions = transactionsDTO.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No Transactions.");
            return;
        }
        System.out.println("All account's transactions:");
        for (TransactionDTO transaction : transactions) {
            System.out.println("(" + " Transaction amount: " + transaction.getAmount() +
                    ", Yaz made at: " + transaction.getYazMade() + ")");
        }
    }

    public static void printLoansList(List<LoanDTO> loanList, String message) throws DataNotFoundException {
        if(loanList.isEmpty())
        {
            System.out.println("No " + message);
            return;
        }

        System.out.println("All account`s " + message);
        for(LoanDTO loan : loanList) {
            printLoan(loan);
        }
    }

    public static void printCustomersDetails(CustomersDTO customersDTO) throws DataNotFoundException {
        List<CustomerDTO> customers = customersDTO.getCustomers();
        for(CustomerDTO customer : customers) {
            printCustomerName(customer);
            System.out.println("Balance: " + customer.getAccount().getBalance());

            printTransactions(customer.getAccount().getTransactionsDTO());
            printLoansList(customer.getRequestedLoans().getLoansList(),"requested loans:");
            printLoansList(customer.getInvestedLoans().getLoansList(), "invested loans:");
        }
    }


    public static void printLoan(LoanDTO loan) throws DataNotFoundException {
        String status = loan.getDetails().getStatus();

        System.out.println("Loan name: " + loan.getDetails().getName() + ", Category: " + loan.getDetails().getCategory() + ", Base amount: " +
                loan.getInterest().getBaseAmount() + ", Total amount: " + loan.getInterest().getFinalAmount() + ", Pay Every: " +
                loan.getPaymentDetails().getCyclesPerPayment() + " Yaz, Interest percent: " +
                loan.getInterest().getPercent() + "%, Status: " + status);

        switch(loan.getDetails().getStatus()) {
            case "PENDING":
                System.out.println("[Money left for becoming Active: " +
                        (loan.getActiveLoanDTO().getAmountToActive()) + "]");
                break;

            case "ACTIVE":
                int cyclesPerPayment = loan.getPaymentDetails().getCyclesPerPayment();
                int nextYaz = loan.getPaymentDetails().getNextPaymentInYaz();//TODO: ?????????
                System.out.println("[Next payment is in: " + nextYaz +
                        "Yaz, Payment amount: " + loan.getPaymentDetails().getNextPaymentAmount() + ".]");
                break;

            case "FINISHED":
                System.out.println("[Yaz Started: " + loan.getYazDetails().getStartedYaz() +
                        ", Yaz Finished: " + loan.getYazDetails().getFinishedYaz() + ".]");
                break;

            case "RISK":
                int missingCycles = loan.getActiveLoanDTO().getMissingCycles();
                System.out.println("[Missing payments: " + missingCycles + ", Missing Amount: "
                        + loan.getActiveLoanDTO().getDeriskAmount() + ".]");
                break;
        }

    }

    public static void printAllLoans(LoansDTO loans) throws DataNotFoundException {
        for(LoanDTO loan : loans.getLoansList()) {
            printLoan((loan));
        }
    }

}
