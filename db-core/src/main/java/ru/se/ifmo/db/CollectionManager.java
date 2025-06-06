package ru.se.ifmo.db;


import ru.se.ifmo.db.entity.Entity;
import ru.se.ifmo.db.entity.User;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Manages a collection of entities and provides CRUD operations.
 *
 * @param <T> type of entity managed by this controller
 */
public interface CollectionManager<T extends Entity> {

    /**
     * Returns the date when this controller was initialized.
     *
     * @return initialization date
     */
    LocalDate getInitializeDate();

    /**
     * Adds the specified entity to the collection.
     *
     * @param entity entity to add
     */
    long add(T entity);

    /**
     * Removes the specified entity from the collection.
     *
     * @param entity entity to remove
     */
    void remove(T entity);

    /**
     * Retrieves the entity with the given identifier.
     *
     * @param id identifier of the entity
     * @return the matching entity, or null if none found
     */
    T get(long id);

    /**
     * Updates the entity with the given identifier.
     *
     * @param id     identifier of the entity to update
     * @param entity new state for the entity
     */
    void update(long id, T entity);

    /**
     * Returns a snapshot of all entities in the collection.
     *
     * <p>Modifications to the returned collection have no effect
     * on the controller's internal state.</p>
     *
     * @return unmodifiable collection of entities
     */
    Collection<T> getCollection();

    /**
     * Removes all entities from the collection.
     */
    void clear();

    /**
     * Adds all entities from the specified collection.
     *
     * @param entities entities to add
     */
    void addAll(Collection<T> entities);
}

