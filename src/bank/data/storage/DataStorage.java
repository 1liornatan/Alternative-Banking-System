package bank.data.storage;

import java.util.Set;

public interface DataStorage<E> {

    E getDataById(int id); // returns null when not exists
    Set<E> getDataByIds(Set<Integer> idSet);
    void addData(E data);
    void addDataSet(Set<E> dataSet);
    boolean isDataExists(int id);



}
