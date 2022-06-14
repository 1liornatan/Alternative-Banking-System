package bank.logic.data.storage;

import bank.logic.impl.exceptions.DataNotFoundException;
import javafx.util.Pair;

import java.util.Collection;

public interface DataStorage<E> {

    E getDataById(String id) throws DataNotFoundException; // returns null when not exists
    Pair<E, Integer> getDataPair(String id) throws DataNotFoundException;
    Collection<E> getDataByIds(Collection<String> idSet) throws DataNotFoundException;
    void addData(E data);
    void addDataSet(Collection<E> dataSet);
    boolean isDataExists(String id);
    Collection<Pair<E, Integer>> getAllPairs();


    void remove(String id) throws DataNotFoundException;
}
