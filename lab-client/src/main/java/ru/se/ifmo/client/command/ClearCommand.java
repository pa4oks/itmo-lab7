package ru.se.ifmo.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;

public class ClearCommand extends DefaultCommand {
    private static final Logger logger = LoggerFactory.getLogger(ClearCommand.class);

    @CommandAction
    public void clear() {
        getProject().getCollectionController().clear();
        logger.info("Collection has been cleared.");
    }

    @Override
    public String getCaption() {
        return "Clear the LabWork collection (removes all elements).";
    }
}
