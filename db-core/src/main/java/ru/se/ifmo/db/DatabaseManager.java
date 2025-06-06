package ru.se.ifmo.db;

/**
 * Handles persistence operations for a {@link CollectionManager}.
 *
 * <p>A {@code DatabaseController} loads entities from a database
 * into an in-memory collection and saves entities from that collection
 * back to the database.</p>
 *
 * @param <T> the type of {@link CollectionManager} managing entities to persist
 */
public interface DatabaseManager<T extends CollectionManager<?>> {

    /**
     * Loads entities from the database into the given collection controller.
     *
     * @param collectionController the controller whose collection is populated
     * @return {@code true} if the load succeeds; {@code false} otherwise
     */
    boolean load(T collectionController);

    /**
     * Saves entities from the given collection controller to the database.
     *
     * @param collectionController the controller providing entities to persist
     * @return {@code true} if the save succeeds; {@code false} otherwise
     */
    boolean save(T collectionController);
}
