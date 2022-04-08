package utils;

import bank.accounts.Account;
import bank.impl.exceptions.DataNotFoundException;
import bank.loans.Loan;
import bank.loans.LoanStatus;
import bank.transactions.Transaction;
import javafx.util.Pair;
import manager.accounts.AccountDTO;
import manager.accounts.AccountsDTO;
import manager.customers.CustomerDTO;
import manager.customers.CustomersDTO;
import manager.loans.LoanDTO;
import manager.transactions.TransactionDTO;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static bank.loans.LoanStatus.*;

public class impl {

    static public void printCustomersNames(CustomersDTO customers) {
        List<CustomerDTO> customersList = new ArrayList<CustomerDTO>();
        System.out.println("All customers names:");
        for(CustomerDTO customer : customersList) {
            System.out.println(customer.getName());
        }
    }

    public static void printCustomerName(CustomerDTO customer) {
            System.out.println(customer.getName());
    }

    public static void printCustomersDetails(CustomersDTO customersDTO) throws DataNotFoundException {
        List<CustomerDTO> customers = customersDTO.getCustomers();
        for(CustomerDTO customer : customers) {
            printCustomerName(customer);
            System.out.println("Balance: " + customer.getAccount().getBalance());
            List<TransactionDTO> transactions = customer.getAccount().getTransactionsDTO().getTransactions();

            System.out.println("All account's transactions:");
            for(TransactionDTO transaction : transactions) {
                System.out.println("(" + transaction.getDescription() +
                        ", Yaz made at: " + transaction.getYazMade() + ")");
            }

            System.out.println("All account's requested loans:");
            for(LoanDTO loan : customer.getRequestedLoans().getLoansList()) {
                printLoan(loan);
            }

            System.out.println("All account's invested loans:");
            for(LoanDTO loan : customer.getInvestedLoans().getLoansList()) {
                printLoan(loan);
            }
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

}
