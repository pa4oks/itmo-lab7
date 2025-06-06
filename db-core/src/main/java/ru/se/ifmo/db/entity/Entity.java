package ru.se.ifmo.db.entity;

/**
 * Represents a persistent entity with a unique identifier.
 */
public interface Entity {

    /**
     * Returns the unique identifier of this entity.
     *
     * @return the entity id
     */
    long getId();

    /**
     * Sets the unique identifier of this entity.
     *
     * @param id the id to assign
     */
    void setId(long id);
}

