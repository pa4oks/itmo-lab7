package ru.se.ifmo.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;
import ru.se.ifmo.lab.model.LabWork;

public class RemoveFirstCommand extends DefaultCommand {
    private static final Logger logger = LoggerFactory.getLogger(RemoveFirstCommand.class);

    @CommandAction
    public void removeFirst() {
        var controller = getProject().getCollectionController();
        var first = controller.getCollection().stream()
                .map(e -> (LabWork) e)
                .findFirst()
                .orElse(null);

        if (first == null) {
            logger.warn("Collection is empty — nothing to remove.");
            return;
        }

        controller.remove(first);
        logger.info("Successfully removed the first LabWork: {}", first);
    }

    @Override
    public String getCaption() {
        return "Remove the first element from the collection.";
    }
}
