package Service;

import java.util.List;

public interface Iservice<T> {
    void insert(T entity);
    void delete(T entity);
    void update(T entity);
    List<T> readAll();
    T readById(int id);
    List<T> readAll(int id);

}
