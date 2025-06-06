package ru.se.ifmo.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;

public class PrintDescending extends DefaultCommand {
    private static final Logger logger = LoggerFactory.getLogger(PrintDescending.class);

    @CommandAction
    public void printDescending() {
        var collection = getProject().getCollectionController().getCollection();
        if (collection.isEmpty()) {
            logger.info("Collection is empty.");
            return;
        }

        logger.info("LabWork elements in descending order (by ID):");
        collection.stream()
                .sorted()
                .forEach(lw -> logger.info("{}", lw));
    }

    @Override
    public String getCaption() {
        return "Print all LabWork elements in descending order by ID.";
    }
}
