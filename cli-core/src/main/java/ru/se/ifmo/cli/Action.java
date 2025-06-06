package ru.se.ifmo.cli;

import java.time.LocalDateTime;

/**
 * Represents an executable task created by a {@link Command}.
 *
 * <p>Encapsulates the operation to perform and records
 * the time when it was created.</p>
 */
public interface Action {

    /**
     * Executes the logic associated with this action.
     */
    void execute();

    /**
     * Returns the command that produced this action.
     *
     * @return the source {@link Command}
     */
    Command getCommand();

    /**
     * Returns the timestamp when this action was created.
     *
     * @return creation time as {@link LocalDateTime}
     */
    LocalDateTime getCreationDate();
}
