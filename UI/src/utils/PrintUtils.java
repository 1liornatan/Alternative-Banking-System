package utils;

import bank.impl.exceptions.DataNotFoundException;
import bank.loans.Loan;
import manager.customers.CustomerDTO;
import manager.customers.CustomersDTO;
import manager.loans.LoanDTO;
import manager.loans.LoanData;
import manager.loans.LoansDTO;
import manager.loans.LoansData;
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

    public static void printLoansList(List<LoanData> loanList, String message) throws DataNotFoundException {
        if(loanList.isEmpty())
        {
            System.out.println("No " + message + ".");
            return;
        }

        System.out.println("All account`s " + message + ":");
        for(LoanData loan : loanList) {
            printLoan(loan);
        }
    }

    public static void printCustomersDetails(CustomersDTO customersDTO) throws DataNotFoundException {
/*        List<CustomerDTO> customers = customersDTO.getCustomers();
        for(CustomerDTO customer : customers) {
            printCustomerName(customer);
            System.out.println("Balance: " + customer.getAccount().getBalance());

            printTransactions(customer.getAccount().getTransactionsDTO());
            printLoansList(customer.getRequestedLoans().getLoansList(),"requested loans");
            printLoansList(customer.getInvestedLoans().getLoansList(), "invested loans");
            System.out.println();
        }*/
    }


    public static void printLoan(LoanData loan) throws DataNotFoundException {
        String status = loan.getStatus();

        System.out.println("Loan name: " + loan.getName() + ", Category: " + loan.getCategory() + ", Base amount: " +
                loan.getInterest() + ", Total amount: " + loan.getInterest() + ", Pay Every: " +
                loan.getCyclesPerPayment() + " Yaz, Interest percent: " +
                loan.getInterest() + "%,  Status: " + status);

        switch(loan.getStatus()) {
            case "PENDING":
                System.out.println("[Money left for becoming Active: " +
                        (loan.getAmountToActive()) + "]");
                break;

            case "ACTIVE":
                int cyclesPerPayment = loan.getCyclesPerPayment();
                int nextYaz = loan.getNextPaymentInYaz();
                System.out.println("[Next payment is in: " + nextYaz +
                        "Yaz, Payment amount: " + loan.getNextPaymentAmount() + ".]");
                break;

            case "FINISHED":
                System.out.println("[Yaz Started: " + loan.getStartedYaz() +
                        ", Yaz Finished: " + loan.getFinishedYaz() + ".]");
                break;

            case "RISK":
                int missingCycles = loan.getMissingCycles();
                System.out.println("[Missing payments: " + missingCycles + ", Missing Amount: "
                        + loan.getDeriskAmount() + ".]");
                break;
        }

    }

    public static void printAllLoans(LoansData loans) throws DataNotFoundException {
        for(LoanData loan : loans.getLoans()) {
            printLoan((loan));
        }
    }

}
