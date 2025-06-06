package ru.se.ifmo.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;
import ru.se.ifmo.db.entity.Entity;

import java.util.Optional;

public class HeadCommand extends DefaultCommand {
    private static final Logger logger = LoggerFactory.getLogger(HeadCommand.class);

    @CommandAction
    public void head() {
        Optional<Entity> first = getProject().getCollectionController().getCollection().stream().findFirst();
        if (first.isPresent()) {
            logger.info("First element in collection:\n{}", first.get());
        } else {
            logger.info("Collection is empty.");
        }
    }

    @Override
    public String getCaption() {
        return "Show the first element of the LabWork collection.";
    }
}
