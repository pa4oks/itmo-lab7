package ru.se.ifmo.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;

public class HelpCommand extends DefaultCommand {
    private final static Logger logger = LoggerFactory.getLogger(HelpCommand.class);

    @CommandAction
    public void help() {
        logger.info("┌────────────── Available Commands ───────────────┐");
        getProject().getCommands().forEach(command -> {
            String name = getProject().getCommands().getNameByType(command.getClass());
            String mask = command.getMask();
            String caption = command.getCaption();
            logger.info("│ {} - {}", String.format("%-12s", name), String.format("%-25s", mask));
            logger.info("│    {}", caption);
        });
        logger.info("│ {} - {}", String.format("%-12s", "exit"), String.format("%-25s", "exit"));
        logger.info("│    Exit the program (without saving to file)");
        logger.info("└────────────────────────────────────────────────┘");
    }

    @Override
    public String getCaption() {
        return "Show available commands and their descriptions.";
    }
}
