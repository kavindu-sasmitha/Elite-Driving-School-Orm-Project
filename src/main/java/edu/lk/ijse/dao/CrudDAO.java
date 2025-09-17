package edu.lk.ijse.dao;

import java.util.List;
import java.util.Optional;

// The 'ID' generic type represents the type of the primary key
public interface CrudDAO<T> extends SuperDAO {
    // Returns an Optional to gracefully handle cases where the entity is not found
    Optional<T> findById(Integer id);
    // Returns a List of all entities
    List<T> findAll();
    // Saves a new entity. Returns true on success, false on failure.
    boolean save(T t);
    // Updates an existing entity.
    void update(T t);
    // Deletes an entity by its ID.
    void delete(T t);
}