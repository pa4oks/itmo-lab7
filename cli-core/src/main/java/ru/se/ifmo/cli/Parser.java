package ru.se.ifmo.cli;

import java.util.List;

/**
 * Parser that converts command-line tool arguments into executable actions.
 *
 * <p>Responsible for interpreting user input and mapping it to commands with arguments.
 * Maintains a history of successfully parsed actions.
 */
public interface Parser {

    /**
     * Returns the history of previously parsed actions.
     *
     * @return list of actions that were successfully parsed and executed
     */
    List<Action> getHistory();

    /**
     * Parses the given arguments into a list of executable actions.
     *
     * @param args the input arguments
     * @return array of parsed actions
     * @throws IllegalArgumentException if input is invalid or incomplete
     */
    Action[] parse(String[] args);

    /**
     * Returns the project associated with this parser.
     *
     * @return the project instance, or null if not set
     */
    Project getProject();
}
