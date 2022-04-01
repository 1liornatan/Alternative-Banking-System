package bank;

public interface Bank {
    void loadData(String filename);

    //    int createLoan(int ownerId, float amount, String category); // returns loan's id
    int withdraw(int accountId, float amount, String description); // returns transaction's id
    int deposit(int accountId, float amount, String description); // returns transaction's id
    int createAccount(String name, float balance); // returns account's id
}
