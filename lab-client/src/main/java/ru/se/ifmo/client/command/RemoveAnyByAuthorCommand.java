package ru.se.ifmo.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;
import ru.se.ifmo.lab.model.Color;
import ru.se.ifmo.lab.model.Country;
import ru.se.ifmo.lab.model.LabWork;
import ru.se.ifmo.lab.model.Location;
import ru.se.ifmo.lab.model.Person;

public class RemoveAnyByAuthorCommand extends DefaultCommand {
    private static final Logger logger = LoggerFactory.getLogger(RemoveAnyByAuthorCommand.class);

    @CommandAction
    public void remove() {
        logger.info("Starting 'remove_any_by_author' command execution");

        Person author = new Person();

        author.setName(requestInput(
                getScanner()::nextCommand,
                "Enter author name: ",
                "Invalid name."
        ));

        author.setWeight(requestInput(
                getScanner()::nextInt,
                "Enter weight (> 0): ",
                "Invalid weight."
        ));

        author.setEyeColor(requestInput(
                () -> getScanner().nextEnumSafe(Color.class),
                "Enter eye color: ",
                "Invalid color."
        ));

        author.setHairColor(requestInputNullable(
                () -> getScanner().nextEnumSafe(Color.class),
                "Enter hair color (or 'null'): ",
                "Invalid color."
        ));

        author.setNationality(requestInput(
                () -> getScanner().nextEnumSafe(Country.class),
                "Enter nationality: ",
                "Invalid country."
        ));

        boolean hasLocation = requestInput(() -> {
            System.out.print("Author has location? (yes/no): ");
            String s = getScanner().nextCommand().trim().toLowerCase();
            if (!s.equals("yes") && !s.equals("no")) throw new IllegalArgumentException();
            return s.equals("yes");
        }, "", "Please enter 'yes' or 'no'.");

        if (hasLocation) {
            Location loc = new Location();
            loc.setX(requestInput(getScanner()::nextLong, "Enter location X: ", "Invalid long."));
            loc.setY(requestInput(getScanner()::nextInt, "Enter location Y: ", "Invalid int."));
            loc.setZ(requestInput(getScanner()::nextFloat, "Enter location Z: ", "Invalid float."));
            loc.setName(requestInput(getScanner()::nextCommand, "Enter location name: ", "Invalid name."));
            author.setLocation(loc);
        }

        var lab = getProject().getCollectionController().getCollection().stream()
                .map(e -> (LabWork) e)
                .filter(e -> e.getAuthor().equals(author))
                .findFirst()
                .orElse(null);

        if (lab == null) {
            logger.info("No matching LabWork with given author found.");
        } else {
            getProject().getCollectionController().remove(lab);
            logger.info("LabWork with author {} was removed.", author.getName());
        }
    }

    @Override
    public String getCaption() {
        return "Remove any LabWork whose author matches the specified one.";
    }
}
