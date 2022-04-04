package bank.data.storage;

import javafx.util.Pair;

import java.util.Collection;

public interface DataStorage<E> {

    E getDataById(int id); // returns null when not exists
    Pair<E, Integer> getDataPair(int id);
    Collection<E> getDataByIds(Collection<Integer> idSet);
    void addData(E data);
    void addDataSet(Collection<E> dataSet);
    boolean isDataExists(int id);
    Collection<Pair<E, Integer>> getAllPairs();



}
