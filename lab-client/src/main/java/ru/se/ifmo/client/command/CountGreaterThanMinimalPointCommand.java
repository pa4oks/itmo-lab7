package ru.se.ifmo.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;
import ru.se.ifmo.lab.model.LabWork;

public class CountGreaterThanMinimalPointCommand extends DefaultCommand {
    private static final Logger logger = LoggerFactory.getLogger(CountGreaterThanMinimalPointCommand.class);

    @CommandAction
    public void count(Double threshold) {
        var count = getProject().getCollectionController().getCollection().stream()
                .map(e -> (LabWork) e)
                .filter(lw -> lw.getMinimalPoint() != null && lw.getMinimalPoint() > threshold)
                .count();

        logger.info("Number of LabWork elements with minimalPoint > {}: {}", threshold, count);
    }

    @Override
    public String getCaption() {
        return "Count elements with minimalPoint greater than the specified value.";
    }

    @Override
    public String getMask() {
        return "count_greater_than_minimal_point <value>";
    }
}
