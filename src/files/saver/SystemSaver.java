package files.saver;

import bank.Bank;
import bank.data.storage.DataStorage;
import javafx.util.Pair;

import java.io.*;
import java.util.Collection;

public class SystemSaver implements Saver {
    Bank bank;

    public SystemSaver(Bank bank) {
        this.bank = bank;
    }

    @Override
    public void saveToFile(String path) {
        try (ObjectOutputStream dataOut = new ObjectOutputStream(
             new FileOutputStream(path))) {
            dataOut.writeInt(bank.getTimeHandler().getPreviousTime());
            dataOut.writeInt(bank.getTimeHandler().getCurrentTime());
            dataOut.writeInt(bank.getCategories().size());
            dataOut.writeObject(bank.getCategories());
            dataOut.writeObject(bank.getCustomersAccounts());
            dataOut.writeObject(bank.getLoanAccounts());
            dataOut.writeObject(bank.getLoans());
            dataOut.writeObject(bank.getTransactions());


            /*writeMap(dataOut, bank.getCustomersAccounts());
            writeMap(dataOut, bank.getLoanAccounts());
            writeMap(dataOut, bank.getTransactions());
            writeMap(dataOut, bank.getLoans());*/

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void writeMap(ObjectOutputStream dataOut, DataStorage dataStorage) throws IOException {
        Collection<Pair> pairCollection = dataStorage.getAllPairs();
        dataOut.writeInt(pairCollection.size());
        for(Pair p : pairCollection) {
            dataOut.writeObject(p);
        }
    }

    @Override
    public void loadFile(String path) {
        try(ObjectInputStream dataIn = new ObjectInputStream(
                new FileInputStream(path))) {

            int prevYaz = dataIn.readInt();
            int currYaz = dataIn.readInt();
            int categoriesAmount = dataIn.readInt();

            Object categories = dataIn.readObject();
            Object customers = dataIn.readObject();
            Object loanAccounts = dataIn.readObject();
            Object loans = dataIn.readObject();
            Object transactions = dataIn.readObject();

            System.out.println(customers.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
