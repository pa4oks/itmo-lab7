package ru.se.ifmo.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.Application;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ExecuteScriptCommand extends DefaultCommand {
    private static final Logger logger = LoggerFactory.getLogger(ExecuteScriptCommand.class);

    @CommandAction
    public void execute(String path) {
        File file = new File(path);
        if (!file.exists()) {
            logger.warn("Script file not found: {}", path);
            return;
        }
        if (!file.canRead()) {
            logger.warn("Script file is not readable: {}", path);
            return;
        }

        ArrayList<String> commands = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.lines()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(commands::add);

            for (String cmd : commands) {
                if (cmd.startsWith(getProject().getCommands().getNameByType(getClass()))) {
                    logger.warn("Recursive script execution is not allowed: {}", cmd);
                    return;
                }
            }
        } catch (IOException e) {
            logger.error("Failed to read script file: {}", path, e);
            return;
        }

        for (String cmd : commands) {
            try {
                Application.execute(cmd.split(" "));
            } catch (Exception ex) {
                logger.error("Failed to execute script command: {}", cmd, ex);
            }
        }
    }

    @Override
    public String getCaption() {
        return "Execute a series of commands from the specified script file.";
    }

    @Override
    public String getMask() {
        return "%s <file_path>".formatted(getProject().getCommands().getNameByType(getClass()));
    }
}
