package ru.se.ifmo.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;
import ru.se.ifmo.lab.model.LabWork;

public class InfoCommand extends DefaultCommand {
    private static final Logger logger = LoggerFactory.getLogger(InfoCommand.class);

    @CommandAction
    public void info() {
        var controller = getProject().getCollectionController();

        String type = LabWork.class.getSimpleName();
        int size = controller.getCollection().size();
        var date = controller.getInitializeDate();

        logger.info("┌────────────── Collection Info ──────────────┐");
        logger.info("│ Type           : {}                         ", type);
        logger.info("│ Elements count : {}                         ", size);
        logger.info("│ Initialized at : {}                         ", date);
        logger.info("└──────────────────────────────────────────────┘");
    }

    @Override
    public String getCaption() {
        return "Show information about the LabWork collection.";
    }

    @Override
    public String getMask() {
        return "info";
    }
}
