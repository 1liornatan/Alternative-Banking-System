package manager.accounts;

import manager.transactions.TransactionDTO;
import manager.transactions.TransactionsDTO;

public class AccountDTO {
    String name;
    int balance;
    TransactionsDTO transactionsDTO;

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public AccountDTO(String name, int balance, TransactionsDTO transactions) {
        this.name = name;
        this.balance = balance;
        this.transactionsDTO = transactions;
    }

    public TransactionsDTO getTransactionsDTO() {
        return transactionsDTO;
    }

    @Override
    public String toString() {
        return "Account Name: " + name +
                ", Balance: " + balance +
                ".";
    }
}

