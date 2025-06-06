package ru.se.ifmo.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.cli.CommandAction;
import ru.se.ifmo.cli.DefaultCommand;
import ru.se.ifmo.lab.model.Color;
import ru.se.ifmo.lab.model.Coordinates;
import ru.se.ifmo.lab.model.Country;
import ru.se.ifmo.lab.model.Difficulty;
import ru.se.ifmo.lab.model.LabWork;
import ru.se.ifmo.lab.model.Location;
import ru.se.ifmo.lab.model.Person;

import java.lang.reflect.Field;
import java.util.Date;

public class UpdateIdCommand extends DefaultCommand {
    private static final Logger logger = LoggerFactory.getLogger(UpdateIdCommand.class);

    @CommandAction
    public void update(Integer id) {
        var controller = getProject().getCollectionController();
        var collection = controller.getCollection().stream().map(t -> ((LabWork) t)).toList();

        LabWork lab = collection.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);

        if (lab == null) {
            logger.warn("No LabWork with id = {}", id);
            return;
        }

        Field field = promptForField(LabWork.class);

        try {
            field.setAccessible(true);

            switch (field.getName()) {
                case "name" -> {
                    String newName = requestInput(getScanner()::nextCommand, "Enter new name: ", "Invalid name.");
                    lab.setName(newName);
                }
                case "minimalPoint" -> {
                    Double newPoint = requestInputNullable(
                            Double::parseDouble,
                            "Enter new minimal point (or 'null'): ",
                            "Invalid number."
                    );
                    lab.setMinimalPoint(newPoint);
                }
                case "difficulty" -> {
                    Difficulty difficulty = requestInputNullable((unused) -> getScanner().nextEnumSafe(Difficulty.class),
                            "Enter new difficulty (or 'null'): ", "Invalid difficulty.");
                    lab.setDifficulty(difficulty);
                }
                case "coordinates" -> {
                    float x = requestInput(getScanner()::nextFloat, "Enter X (> -204): ", "Invalid X.");
                    float y = requestInput(getScanner()::nextFloat, "Enter Y: ", "Invalid Y.");
                    Coordinates coords = new Coordinates();
                    coords.setX(x);
                    coords.setY(y);
                    lab.setCoordinates(coords);
                }
                case "author" -> {
                    Person p = new Person();

                    p.setName(requestInput(getScanner()::nextCommand, "Enter author name: ", "Invalid name."));
                    p.setWeight(requestInput(getScanner()::nextInt, "Enter weight (> 0): ", "Invalid weight."));
                    p.setEyeColor(requestInput(() -> getScanner().nextEnumSafe(Color.class), "Enter eye color: ", "Invalid color."));
                    p.setHairColor(requestInputNullable((unused) -> getScanner().nextEnumSafe(Color.class), "Enter hair color (or 'null'): ", "Invalid color."));
                    p.setNationality(requestInput(() -> getScanner().nextEnumSafe(Country.class), "Enter nationality: ", "Invalid nationality."));

                    boolean hasLocation = requestInput(() -> {
                        System.out.print("Change location? (yes/no): ");
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
                        p.setLocation(loc);
                    }

                    lab.setAuthor(p);
                }
                default -> logger.warn("Field update not supported: {}", field.getName());
            }

            lab.setCreationDate(new Date());
            controller.update(id, lab);
            logger.info("LabWork with id={} successfully updated.", id);

        } catch (Exception e) {
            logger.error("Update failed", e);
        }
    }

    private <T> T requestInputNullable(Supplier<T> supplier, String prompt, String error) {
        return requestInput(() -> {
            String raw = getScanner().nextCommand();
            if ("null".equalsIgnoreCase(raw)) return null;
            return supplier.getWith(raw);
        }, prompt, error);
    }

    @FunctionalInterface
    private interface Supplier<T> {
        T getWith(String raw);
    }

    @Override
    public String getCaption() {
        return "Update the LabWork element with the specified ID.";
    }

    @Override
    public String getMask() {
        return "update <id> {field update}";
    }
}
