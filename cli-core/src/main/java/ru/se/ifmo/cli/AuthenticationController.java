package ru.se.ifmo.cli;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.db.Encoder;
import ru.se.ifmo.db.entity.User;
import ru.se.ifmo.db.table.UserTable;

import java.io.Console;
import java.util.InputMismatchException;

public final class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final CommandsScanner scanner;
    private final UserTable userTable;
    private final Encoder encoder;

    @Inject
    public AuthenticationController(UserTable userTable, CommandsScanner scanner, Encoder encoder) {
        this.userTable = userTable;
        this.scanner = scanner;
        this.encoder = encoder;
    }

    public User authenticate() {
        User user;
        boolean retry = false;

        while (true) {
            if (retry) {
                logger.info("Invalid input. Please try again.");
            } else {
                logger.info("Welcome! Choose an authentication method:");
            }

            logger.info("1. Log in");
            logger.info("2. Register");
            logger.info("Enter option number: ");

            int option;
            try {
                option = scanner.nextInt();
            } catch (IllegalArgumentException e) {
                logger.warn("Input must be a number.");
                continue;
            }

            switch (option) {
                case 1 -> {
                    user = login();
                    if (user != null) return user;
                }
                case 2 -> {
                    user = register();
                    if (user != null) return user;
                }
                default -> logger.warn("Please enter 1 or 2.");
            }

            retry = true;
        }
    }

    private User login() {
        logger.info("Enter your username:");
        String username = scanner.nextCommand();
        String password = readPassword("Enter your password: ");
        if (password == null) return null;

        User user = userTable.getUser(username);
        if (user == null || !encoder.verify(password, user.getPassword())) {
            logger.warn("Invalid username or password.");
            return null;
        }

        logger.info("Logged in successfully as {}.", user.getUsername());
        return user;
    }

    private User register() {
        logger.info("Enter a username:");
        String username = scanner.nextCommand();

        if (username == null || username.isBlank()) {
            logger.warn("Username cannot be empty.");
            return null;
        }

        if (userTable.isExist(username)) {
            logger.warn("Username already exists. Choose another one.");
            return null;
        }

        String password = createPassword();
        if (password == null) return null;

        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        userTable.insertUser(user);

        logger.info("Account created successfully.");
        return user;
    }

    private String readPassword(String prompt) {
        Console console = System.console();
        String input;

        if (console != null) {
            char[] passwordChars = console.readPassword(prompt);
            input = passwordChars == null ? null : new String(passwordChars);
        } else {
            logger.info(prompt);
            input = scanner.nextCommand();
        }

        if (input == null || input.isBlank()) {
            logger.warn("Password cannot be empty.");
            return null;
        }

        return input;
    }

    private String createPassword() {
        String password = readPassword("Create a password: ");
        if (password == null) return null;

        if (password.length() < 6) {
            logger.warn("Password must be at least 6 characters.");
            return null;
        }

        if (!password.matches(".*\\d.*")) {
            logger.warn("Password must contain at least one digit.");
            return null;
        }

        String confirmPassword = readPassword("Confirm your password: ");
        if (!password.equals(confirmPassword)) {
            logger.warn("Passwords do not match.");
            return null;
        }

        return password;
    }
}
