package manager.accounts;

import bank.accounts.Account;

public class AccountDTO {
    String name;
    int balance;

    public AccountDTO(Account account) {
        name = account.getId();
        balance = account.getBalance();
    }

    @Override
    public String toString() {
        return "Account Name: " + name +
                ", Balance: " + balance +
                ".";
    }
}

