package ru.se.ifmo.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;
import ru.se.ifmo.lab.model.LabWork;

public class RemoveByIdCommand extends DefaultCommand {
    private static final Logger logger = LoggerFactory.getLogger(RemoveByIdCommand.class);

    @CommandAction
    public void remove(Integer id) {
        logger.info("Attempting to remove LabWork with id = {}", id);

        var controller = getProject().getCollectionController();
        var lab = controller.getCollection().stream()
                .map(e -> (LabWork) e)
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);

        if (lab == null) {
            logger.warn("LabWork with id = {} not found", id);
            return;
        }

        controller.remove(lab);
        logger.info("LabWork with id = {} successfully removed", id);
    }

    @Override
    public String getCaption() {
        return "Remove a LabWork element by its ID.";
    }

    @Override
    public String getMask() {
        return "remove_by_id <id>";
    }
}
