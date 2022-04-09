package files.saver;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Saver {
    int getPrevYaz();

    int getCurrYaz();

    Object getCategories();

    Object getCustomers();

    Object getLoanAccounts();

    Object getLoans();

    Object getTransactions();

    void saveToFile(String path) throws IOException;

    void loadFile(String path) throws IOException, ClassNotFoundException;

    boolean isValid();
}
