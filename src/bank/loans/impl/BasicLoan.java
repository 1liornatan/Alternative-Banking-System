package bank.loans.impl;

import bank.loans.Loan;

public class BasicLoan implements Loan {
    private static int idGenerator = 3000;
    private int id, requesterId;
    private String category;
    private float baseAmount;
    private Interest interest;

    BasicLoan(int requesterId) {

    }
}
