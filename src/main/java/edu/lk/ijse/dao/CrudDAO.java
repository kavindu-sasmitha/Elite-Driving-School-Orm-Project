package edu.lk.ijse.dao;

import java.util.List;
import java.util.Optional;


public interface CrudDAO<T> extends SuperDAO {

    Optional<T> findById(Integer id);

    List<T> findAll();

    boolean save(T t);

    void update(T t);

    void delete(T t);
}