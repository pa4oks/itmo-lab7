package ru.se.ifmo.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;

public class CommandsScanner {
    private static final Logger log = LoggerFactory.getLogger(CommandsScanner.class);

    private final Scanner scanner;
    private final String exitString;

    public CommandsScanner(InputStream input, String exitString) {
        this.scanner = new Scanner(Objects.requireNonNull(input));
        this.exitString = Objects.requireNonNull(exitString);
        log.debug("CommandsScanner initialized with exit string '{}'", exitString);
    }

    public CommandsScanner(InputStream input) {
        this(input, "exit");
    }

    public String nextCommand() {
        String line = scanner.nextLine();
        log.debug("Received command input: '{}'", line);
        return checkExit(line);
    }

    public Integer nextInt() {
        return parse(scanner.nextLine(), Integer::parseInt, "int");
    }

    public Double nextDouble() {
        return parse(scanner.nextLine(), Double::parseDouble, "double");
    }

    public Float nextFloat() {
        return parse(scanner.nextLine(), Float::parseFloat, "float");
    }

    public Long nextLong() {
        return parse(scanner.nextLine(), Long::parseLong, "long");
    }

    public <E extends Enum<E>> E parseEnum(Class<E> enumType, String input) {
        log.debug("Received enum input: '{}'", input);
        for (E constant : enumType.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(input)) {
                log.debug("Parsed enum value: {}", constant);
                return constant;
            }
        }
        log.warn("Invalid enum value: {}", input);
        throw new IllegalArgumentException("Invalid enum value: " + input);
    }

    public <E extends Enum<E>> E nextEnumSafe(Class<E> enumType) {
        String options = String.join(" ",
                Arrays.stream(enumType.getEnumConstants()).map(Enum::name).toList());
        log.info("Available options: {}", options);

        String input = checkExit(scanner.nextLine()).trim();
        log.debug("Received enum input: '{}'", input);

        for (E constant : enumType.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(input)) {
                log.debug("Parsed enum value: {}", constant);
                return constant;
            }
        }

        log.warn("Invalid enum value: {}", input);
        throw new IllegalArgumentException("Invalid enum value: " + input);
    }

    private String checkExit(String value) {
        if (exitString.equalsIgnoreCase(value.trim())) {
            log.info("Exit command received. Shutting down.");
            System.exit(0);
        }
        return value;
    }

    private <T> T parse(String input, Function<String, T> parser, String typeName) {
        try {
            String checked = checkExit(input);
            T value = parser.apply(checked);
            log.debug("Parsed {}: {}", typeName, value);
            return value;
        } catch (Exception ex) {
            log.warn("Failed to parse {} from '{}'", typeName, input);
            throw new IllegalArgumentException("Failed to parse input: " + input, ex);
        }
    }
}
