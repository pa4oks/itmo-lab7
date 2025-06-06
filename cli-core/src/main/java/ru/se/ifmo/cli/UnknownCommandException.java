package ru.se.ifmo.cli;

/**
 * Thrown when a parser encounters a command string that isn't registered in the command container.
 *
 * <p>This exception typically indicates that the user has entered an invalid or unknown command
 * that hasn't been previously registered via {@link CommandContainer#register(String, Class)}.</p>
 *
 * <p>The {@code unknownCommand} field stores the unrecognized input string, which can be used
 * for logging, user feedback, or error reporting.</p>
 */
public class UnknownCommandException extends RuntimeException {

    private final String unknownCommand;

    /**
     * Constructs a new {@code UnknownCommandException} with the specified unrecognized command and message.
     *
     * @param unknownCommand the command string that couldn't be resolved
     * @param message        the detail message explaining the context of the error
     */
    public UnknownCommandException(String unknownCommand, String message) {
        super(message);
        this.unknownCommand = unknownCommand;
    }

    /**
     * Constructs a new {@code UnknownCommandException} with the specified unrecognized command, message, and cause.
     *
     * @param unknownCommand the command string that couldn't be resolved
     * @param message        the detail message explaining the context of the error
     * @param cause          the underlying cause of the exception
     */
    public UnknownCommandException(String unknownCommand, String message, Throwable cause) {
        super(message, cause);
        this.unknownCommand = unknownCommand;
    }

    /**
     * Returns the unrecognized command string that triggered this exception.
     *
     * @return the invalid or unknown command string
     */
    public String getUnknownCommand() {
        return unknownCommand;
    }
}
