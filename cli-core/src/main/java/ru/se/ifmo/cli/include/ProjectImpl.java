package ru.se.ifmo.cli.include;

import com.google.inject.Inject;
import ru.se.ifmo.cli.CommandContainer;
import ru.se.ifmo.cli.Parser;
import ru.se.ifmo.cli.Project;
import ru.se.ifmo.db.CollectionManager;
import ru.se.ifmo.db.DatabaseManager;
import ru.se.ifmo.db.SQLConnectionController;
import ru.se.ifmo.db.entity.Entity;

import java.util.Objects;

public class ProjectImpl implements Project {
    private final CommandContainer commandContainer;
    private final Parser parser;
    private final DatabaseManager<?> databaseManager;
    private final CollectionManager<?> collectionManager;

    @Inject
    public ProjectImpl(CommandContainer commandContainer,
                       Parser parser,
                       DatabaseManager<?> databaseManager,
                       CollectionManager<?> collectionManager) {
        this.commandContainer = Objects.requireNonNull(commandContainer, "commandContainer must not be null");
        this.parser = Objects.requireNonNull(parser, "parser must not be null");
        this.databaseManager = Objects.requireNonNull(databaseManager, "databaseController must not be null");
        this.collectionManager = Objects.requireNonNull(collectionManager, "collectionController must not be null");

        if (commandContainer instanceof CommandContainerImpl impl) {
            impl.setProject(this);
        }
    }

    @Override
    public CommandContainer getCommands() {
        return commandContainer;
    }

    @Override
    public Parser getParser() {
        return parser;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends CollectionManager<?>> DatabaseManager<T> getDatabaseController() {
        return (DatabaseManager<T>) databaseManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> CollectionManager<T> getCollectionController() {
        return (CollectionManager<T>) collectionManager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectImpl that)) {
            return false;
        }

        return Objects.equals(commandContainer, that.commandContainer)
            && Objects.equals(parser, that.parser)
            && Objects.equals(databaseManager, that.databaseManager)
            && Objects.equals(collectionManager, that.collectionManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandContainer, parser, databaseManager, collectionManager);
    }

    @Override
    public String toString() {
        return "ProjectImpl{" +
            "commandContainer=" + commandContainer +
            ", parser=" + parser +
            ", databaseController=" + databaseManager +
            ", collectionController=" + collectionManager +
            '}';
    }
}
