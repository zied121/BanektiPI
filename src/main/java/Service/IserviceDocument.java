package Service;

import java.util.List;

public interface IserviceDocument<T> {
    void insert(T entity);
    void delete(T entity);
    void update(T entity);

    List<T> readAll();

    T readById(int id);

}