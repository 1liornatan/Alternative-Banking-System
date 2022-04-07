package manager.accounts;

public class AccountDTO {
    String name;
    int balance;

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public AccountDTO(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account Name: " + name +
                ", Balance: " + balance +
                ".";
    }
}

