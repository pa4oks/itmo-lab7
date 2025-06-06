package ru.se.ifmo.cli;

import java.util.function.Consumer;

/**
 * Container for managing command-line tool command instances.
 *
 * <p>Each command is registered under a unique string key and can later be retrieved
 * or iterated over. Command instances must implement the {@link Command} interface.
 */
public interface CommandContainer {

    /**
     * Registers a command class under a specific name.
     *
     * @param command the unique name used to invoke the command
     * @param commandClass the class implementing the command
     * @throws UnsupportedOperationException if the name is empty or already registered
     * @throws IllegalArgumentException if the class cannot be instantiated
     */
    void register(String command, Class<? extends Command> commandClass);

    /**
     * Returns the command instance associated with the given name.
     *
     * @param command the command name
     * @return the corresponding command instance
     * @throws IllegalArgumentException if the command is not registered
     */
    Command get(String command);

    /**
     * Returns the name associated with the given command type.
     *
     * @param commandType the type of the command
     * @return the name of the registered command
     * @throws IllegalArgumentException if no matching command is found
     */
    String getNameByType(Class<?> commandType);

    /**
     * Returns whether a command with the given name is registered.
     *
     * @param name the command name to check
     * @return true if the command exists, false otherwise
     */
    boolean contains(String name);

    /**
     * Performs the given action for each registered command.
     *
     * @param action the operation to apply to each command
     */
    void forEach(Consumer<? super Command> action);

    /**
     * Returns whether the container has no registered commands.
     *
     * @return true if the container is empty
     */
    boolean isEmpty();

    /**
     * Returns the project associated with this container.
     *
     * @return the project instance, or null if not set
     */
    Project getProject();
}
