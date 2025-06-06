package ru.se.ifmo.cli;

import ru.se.ifmo.db.CollectionManager;
import ru.se.ifmo.db.DatabaseManager;
import ru.se.ifmo.db.SQLConnectionController;
import ru.se.ifmo.db.entity.Entity;

/**
 * Project context for a command-line tool-based app.
 *
 * <p>Provides access to the command container, argument parser,
 * and database controllers for entities and collections.
 */
public interface Project {

    /**
     * Returns the container that holds all registered commands.
     *
     * @return the command container instance
     */
    CommandContainer getCommands();

    /**
     * Returns the parser that converts command-line tool arguments into executable actions.
     *
     * @return the argument parser instance
     */
    Parser getParser();

    /**
     * Returns the database controller for the given collection type.
     *
     * @param <T> the type of the collection controller
     * @return the database controller instance
     */
    <T extends CollectionManager<?>> DatabaseManager<T> getDatabaseController();

    /**
     * Returns the collection controller for the given entity type.
     *
     * @param <T> the type of the entity
     * @return the collection controller instance
     */
    <T extends Entity> CollectionManager<T> getCollectionController();
}
