package ru.se.ifmo.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;

public class ShowCommand extends DefaultCommand {
    private static final Logger logger = LoggerFactory.getLogger(ShowCommand.class);

    @CommandAction
    public void show() {
        logger.info("All LabWorks:");
        getProject().getCollectionController().getCollection()
                .stream()
                .map(Object::toString)
                .forEach(logger::info);
    }

    @Override
    public String getCaption() {
        return "Display all LabWork elements currently in the collection.";
    }
}
