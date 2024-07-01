package Service;

import entite.Reclamation;

import java.util.List;

public interface IserviceReclamation<T> {
    void insertPST(T t);

    void insert(T t);
    void delete(T t);
    void update(T t);
    List<T> readAll(T t);
    List<T> readAll();


    List<Reclamation> readAll(Reclamation Reclamation);

    T readbyid(int id);
}
