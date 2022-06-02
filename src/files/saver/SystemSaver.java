package files.saver;

import bank.Bank;

import java.io.*;

public class SystemSaver implements Saver {
    final Bank bank;
    private boolean valid;
    private int prevYaz, currYaz;
    private Object categories, customers, loanAccounts, loans, transactions;

    @Override
    public int getPrevYaz() {
        return prevYaz;
    }

    @Override
    public int getCurrYaz() {
        return currYaz;
    }

    @Override
    public Object getCategories() {
        return categories;
    }

    @Override
    public Object getCustomers() {
        return customers;
    }

    @Override
    public Object getLoanAccounts() {
        return loanAccounts;
    }

    @Override
    public Object getLoans() {
        return loans;
    }

    @Override
    public Object getTransactions() {
        return transactions;
    }

    public SystemSaver(Bank bank) {
        this.bank = bank;
        valid = false;
    }

    @Override
    public void saveToFile(String path) throws IOException {
        ObjectOutputStream dataOut = new ObjectOutputStream(
                new FileOutputStream(path));

        dataOut.writeInt(bank.getTimeHandler().getPreviousTime());
        dataOut.writeInt(bank.getTimeHandler().getCurrentTime());

        dataOut.writeObject(bank.getCategories());
        dataOut.writeObject(bank.getCustomersAccounts());
        dataOut.writeObject(bank.getLoanAccounts());
        dataOut.writeObject(bank.getLoans());
        dataOut.writeObject(bank.getTransactions());

    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void loadFile(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream dataIn = new ObjectInputStream(
                new FileInputStream(path));

        prevYaz = dataIn.readInt();
        currYaz = dataIn.readInt();

        categories = dataIn.readObject();
        customers = dataIn.readObject();
        loanAccounts = dataIn.readObject();
        loans = dataIn.readObject();
        transactions = dataIn.readObject();

        valid = true;

    }
}
