package ru.se.ifmo.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;
import ru.se.ifmo.lab.model.*;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class AddCommand extends DefaultCommand {
    private static final Logger log = LoggerFactory.getLogger(AddCommand.class);

    @CommandAction
    public void add() {
        log.info("Starting 'add' command execution");

        LabWork labWork = new LabWork();

        labWork.setName(requestInput(
                getScanner()::nextCommand,
                "Enter lab work name: ",
                "Name cannot be empty."
        ));

        labWork.setCoordinates(createCoordinates());

        labWork.setMinimalPoint(nullableInput(
                Double::parseDouble,
                "Enter minimal point (or 'null'): ",
                "Minimal point must be a number or 'null'."
        ));

        log.info("Available options: {}", enumOptions(Difficulty.class));
        labWork.setDifficulty(nullableInput(
                s -> getScanner().parseEnum(Difficulty.class, s),
                "Enter difficulty (or 'null'): ",
                "Invalid difficulty."
        ));

        labWork.setAuthor(createAuthor());
        labWork.setCreationDate(new Date());

        log.info("Sending labWork: {}", labWork);
        getProject().getCollectionController().add(labWork);
    }

    private Coordinates createCoordinates() {
        float x = requestInput(getScanner()::nextFloat, "Enter coordinate X (> -204): ", "Invalid X.");
        float y = requestInput(getScanner()::nextFloat, "Enter coordinate Y: ", "Invalid Y.");
        Coordinates c = new Coordinates();
        c.setX(x);
        c.setY(y);
        return c;
    }

    private Person createAuthor() {
        Person p = new Person();

        p.setName(requestInput(
                getScanner()::nextCommand,
                "Enter author name: ",
                "Invalid name."
        ));

        p.setWeight(requestInput(
                getScanner()::nextInt,
                "Enter weight (> 0): ",
                "Invalid weight."
        ));

        log.info("Available options: {}", enumOptions(Color.class));
        p.setEyeColor(requestInput(
                () -> getScanner().parseEnum(Color.class, getScanner().nextCommand()),
                "Enter eye color: ",
                "Invalid color."
        ));

        log.info("Available options: {}", enumOptions(Color.class));
        p.setHairColor(nullableInput(
                s -> getScanner().parseEnum(Color.class, s),
                "Enter hair color (or 'null'): ",
                "Invalid hair color."
        ));

        log.info("Available options: {}", enumOptions(Country.class));
        p.setNationality(nullableInput(
                s -> getScanner().parseEnum(Country.class, s),
                "Enter nationality (or 'null'): ",
                "Invalid nationality."
        ));

        boolean hasLocation = requestInput(
                () -> {
                    String s = getScanner().nextCommand().trim().toLowerCase();
                    if (!s.equals("yes") && !s.equals("no")) throw new IllegalArgumentException();
                    return s.equals("yes");
                },
                "Add location? (yes/no): ",
                "Please enter 'yes' or 'no'."
        );

        if (hasLocation) {
            Location loc = new Location();
            loc.setX(requestInput(getScanner()::nextLong, "Enter location X: ", "Invalid long."));
            loc.setY(requestInput(getScanner()::nextInt, "Enter location Y: ", "Invalid int."));
            loc.setZ(requestInput(getScanner()::nextFloat, "Enter location Z: ", "Invalid float."));
            loc.setName(requestInput(getScanner()::nextCommand, "Enter location name: ", "Invalid name."));
            p.setLocation(loc);
        }

        return p;
    }

    private <E extends Enum<E>> String enumOptions(Class<E> enumType) {
        return Arrays.stream(enumType.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining(" "));
    }

    @Override
    public String getCaption() {
        return "Add a new LabWork element to the collection.";
    }

    @Override
    public String getMask() {
        return "add";
    }
}
