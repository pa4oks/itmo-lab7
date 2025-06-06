package ru.se.ifmo.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;
import ru.se.ifmo.lab.model.Coordinates;
import ru.se.ifmo.lab.model.LabWork;

public class RemoveLowerCommand extends DefaultCommand {
    private static final Logger logger = LoggerFactory.getLogger(RemoveLowerCommand.class);

    @CommandAction
    public void removeLower() {
        logger.info("Starting removeLower command");

        float x = requestInput(getScanner()::nextFloat, "Enter X: ", "Invalid X");
        float y = requestInput(getScanner()::nextFloat, "Enter Y: ", "Invalid Y");

        Coordinates reference = new Coordinates();
        reference.setX(x);
        reference.setY(y);

        LabWork threshold = new LabWork();
        threshold.setCoordinates(reference);

        var controller = getProject().getCollectionController();
        var toRemove = controller.getCollection().stream()
                .map(e -> (LabWork) e)
                .filter(e -> e.compareTo(threshold) < 0)
                .toList();

        for (LabWork lw : toRemove) {
            controller.remove(lw);
            logger.debug("Removed: {}", lw);
        }

        logger.info("Removed {} elements lower than ({}, {})", toRemove.size(), x, y);
    }

    @Override
    public String getCaption() {
        return "Remove all LabWork elements less than the given one (by coordinates).";
    }
}
