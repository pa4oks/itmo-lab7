package ru.se.ifmo.lab.server.db;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.db.DatabaseManager;
import ru.se.ifmo.lab.model.Color;
import ru.se.ifmo.lab.model.Coordinates;
import ru.se.ifmo.lab.model.Country;
import ru.se.ifmo.lab.model.Difficulty;
import ru.se.ifmo.lab.model.LabWork;
import ru.se.ifmo.lab.model.Location;
import ru.se.ifmo.lab.model.Person;
import ru.se.ifmo.lab.util.JacksonUtil;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

public class LabWorkDatabaseManager implements DatabaseManager<LabWorkCollectionManager> {
    private static final Logger logger = LoggerFactory.getLogger(LabWorkDatabaseManager.class);

    private final Path file = Paths.get("data", "labworks.csv");
    private final CsvMapper mapper = JacksonUtil.createCsvMapper();
    private final CsvSchema schema = JacksonUtil.schemaFor(FlatLabWorkDto.class);

    public LabWorkDatabaseManager() {
        try {
            Files.createDirectories(file.getParent());
            if (Files.notExists(file)) {
                Files.createFile(file);
                logger.info("Created empty CSV file at {}", file.toAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Failed to initialize database file: {}", file, e);
        }
    }

    @Override
    public boolean load(LabWorkCollectionManager collectionController) {
        try {
            if (Files.exists(file) && Files.size(file) > 0) {
                try (Reader reader = Files.newBufferedReader(file);
                     MappingIterator<FlatLabWorkDto> it = mapper.readerFor(FlatLabWorkDto.class).with(schema).readValues(reader)) {
                    collectionController.clear();
                    collectionController.addAll(it.readAll().stream().map(FlatLabWorkDto::toLabWork).toList());
                    logger.info("Loaded {} LabWork records from {}", collectionController.getCollection().size(), file);
                }
            }
            return true;
        } catch (IOException e) {
            logger.error("Failed to load CSV from {}", file, e);
            return false;
        }
    }

    @Override
    public boolean save(LabWorkCollectionManager collectionController) {
        try (Writer writer = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
             SequenceWriter seq = mapper.writerFor(FlatLabWorkDto.class).with(schema).writeValues(writer)) {
            seq.writeAll(collectionController.getCollection().stream().map(FlatLabWorkDto::from).toList());
            logger.info("Saved {} LabWork records to {}", collectionController.getCollection().size(), file);
            return true;
        } catch (IOException e) {
            logger.error("Failed to save CSV to {}", file, e);
            return false;
        }
    }

    public static class FlatLabWorkDto {
        public long id;
        public String name;
        public float coordX;
        public float coordY;
        public long creationDate;
        public double minimalPoint;
        public String difficulty;
        public String authorName;
        public int authorWeight;
        public String authorEyeColor;
        public String authorHairColor;
        public String authorNationality;
        public Long locationX;
        public Integer locationY;
        public String locationName;

        public static FlatLabWorkDto from(LabWork lw) {
            FlatLabWorkDto dto = new FlatLabWorkDto();
            dto.id = lw.getId();
            dto.name = lw.getName();
            dto.coordX = lw.getCoordinates().getX();
            dto.coordY = lw.getCoordinates().getY();
            dto.creationDate = lw.getCreationDate().getTime();
            dto.minimalPoint = lw.getMinimalPoint();
            dto.difficulty = lw.getDifficulty().name();

            Person person = lw.getAuthor();
            dto.authorName = person.getName();
            dto.authorWeight = person.getWeight();
            dto.authorEyeColor = person.getEyeColor().name();
            dto.authorHairColor = person.getHairColor().name();
            dto.authorNationality = person.getNationality().name();

            if (person.getLocation() != null) {
                dto.locationX = person.getLocation().getX();
                dto.locationY = person.getLocation().getY();
                dto.locationName = person.getLocation().getName();
            }

            return dto;
        }

        public LabWork toLabWork() {
            LabWork lw = new LabWork();
            lw.setId(id);
            lw.setName(name);
            Coordinates coordinates = new Coordinates();
            coordinates.setX(coordX);
            coordinates.setY(coordY);
            lw.setCoordinates(coordinates);
            lw.setCreationDate(new Date(creationDate));
            lw.setMinimalPoint(minimalPoint);
            lw.setDifficulty(Difficulty.valueOf(difficulty));

            Person person = new Person();
            person.setName(authorName);
            person.setWeight(authorWeight);
            person.setEyeColor(Color.valueOf(authorEyeColor));
            person.setHairColor(Color.valueOf(authorHairColor));
            person.setNationality(Country.valueOf(authorNationality));

            if (locationX != null && locationY != null && locationName != null) {
                Location location = new Location();
                location.setX(locationX);
                location.setY(locationY);
                location.setName(locationName);
                person.setLocation(location);
            }

            lw.setAuthor(person);
            return lw;
        }
    }
}
