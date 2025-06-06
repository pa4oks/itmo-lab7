package ru.se.ifmo.cli;

/**
 * Represents a command that can be executed within a project context.
 *
 * <p>A {@code Command} provides metadata (caption, usage mask), expected parameter types,
 * and the ability to create an {@link Action} instance for execution based on user input.</p>
 */
public interface Command {

    /**
     * Returns the project that owns this command.
     *
     * @return the associated {@link Project}, or {@code null} if not set
     */
    Project getProject();

    /**
     * Returns the parameter types required by this command's action method.
     *
     * @return an array of expected parameter types
     */
    Class<?>[] getActionParams();

    /**
     * Creates an {@link Action} instance using the provided user input parameters.
     *
     * @param params the user-provided arguments
     * @return the corresponding {@link Action} instance
     */
    Action getAction(Object... params);

    /**
     * Returns a human-readable caption that describes the command.
     *
     * @return the caption of the command
     */
    String getCaption();

    /**
     * Returns the usage mask (syntax) for the command.
     *
     * @return a string describing how to use the command
     */
    String getMask();
}
