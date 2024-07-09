package Service;

import entite.Reclamation;
import entite.Repond;

import java.util.List;

public interface Iservice<T> {
    void delete(Repond Repond);

    void update(Repond Repond);

    void insert(T entity, int id_user);
    void delete(T entity, int id_user);
    void update(T entity, int id_user);

    void insert(Reclamation Reclamation);

    void delete(Reclamation Reclamation);

    void update(Reclamation Reclamation);

    List<T> readAll();

    List<T> readAll(int id_user);
    T readById(int id);
}
