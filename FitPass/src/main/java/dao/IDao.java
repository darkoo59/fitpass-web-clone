package dao;

import java.io.IOException;
import java.util.ArrayList;

public interface IDao<T> {
    ArrayList<T> getAll() throws IOException;
    T get(String id) throws IOException;
    void save(ArrayList<T> objs) throws IOException;
    void delete(T obj) throws IOException;
    void update(T obj) throws IOException;

    int getNewId() throws IOException;
}
