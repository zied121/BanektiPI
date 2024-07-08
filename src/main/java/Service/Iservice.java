package Service;

import java.util.List;

public interface Iservice<T> {
    void insert(T entity, int id_user);
    void delete(T entity, int id_user);
    void update(T entity, int id_user);
    List<T> readAll(int id_user);
    T readById(int id);
}
