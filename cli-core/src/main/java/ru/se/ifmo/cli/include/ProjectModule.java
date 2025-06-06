package ru.se.ifmo.cli.include;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import ru.se.ifmo.cli.CommandContainer;
import ru.se.ifmo.cli.Parser;
import ru.se.ifmo.cli.Project;
import ru.se.ifmo.db.CollectionManager;
import ru.se.ifmo.db.DatabaseManager;
import ru.se.ifmo.db.SQLConnectionController;

import java.sql.Connection;
import java.util.Objects;

/**
 * Guice module for wiring command-line tool app components.
 *
 * <p>Binds the given CollectionController and DatabaseController
 * as instances and configures CommandContainer, Parser, and Project
 * as singletons.</p>
 */
public class ProjectModule extends AbstractModule {
    private final CollectionManager<?> collectionManager;
    private final DatabaseManager<?> databaseManager;

    /**
     * @param collectionManager controller for entity collections; must not be null
     * @param databaseManager   controller for database access; must not be null
     */
    public ProjectModule(CollectionManager<?> collectionManager,
                         DatabaseManager<?> databaseManager) {
        this.collectionManager = Objects.requireNonNull(collectionManager,
            "collectionController must not be null");
        this.databaseManager = Objects.requireNonNull(databaseManager,
            "databaseController must not be null");
    }

    /**
     * Binds controllers by instance and sets up singleton scopes
     * for CommandContainer, Parser, and Project implementations.
     */
    @Override
    protected void configure() {
        bind(new TypeLiteral<CollectionManager<?>>() {})
            .toInstance(collectionManager);

        bind(new TypeLiteral<DatabaseManager<?>>() {})
            .toInstance(databaseManager);

        bind(CommandContainer.class)
            .to(CommandContainerImpl.class)
            .in(Singleton.class);

        bind(Parser.class)
            .to(ParserImpl.class)
            .in(Singleton.class);

        bind(Project.class)
            .to(ProjectImpl.class)
            .in(Singleton.class);
    }

    @Provides
    @Singleton
    public Connection provideConnection(SQLConnectionController controller) {
        return controller.getConnection();
    }
}
