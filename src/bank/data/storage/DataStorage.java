package bank.data.storage;

import java.util.Collection;
import java.util.Set;

public interface DataStorage<E> {

    E getDataById(int id); // returns null when not exists
    Collection<E> getDataByIds(Collection<Integer> idSet);
    void addData(E data);
    void addDataSet(Collection<E> dataSet);
    boolean isDataExists(int id);
    Collection<E> getAll();



}
