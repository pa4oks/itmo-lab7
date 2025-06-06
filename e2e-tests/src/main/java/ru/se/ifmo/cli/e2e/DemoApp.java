package ru.se.ifmo.cli.e2e;

import ru.se.ifmo.cli.Application;
import ru.se.ifmo.cli.Project;
import ru.se.ifmo.db.CollectionController;
import ru.se.ifmo.db.DatabaseController;
import ru.se.ifmo.db.entity.Entity;

public class DemoApp extends Application {
    private InMemoryCollectionController collectionController;
    private NoOpDatabaseController databaseController;

    public static DemoApp launch() {
        return Application.launch();
    }

    @Override
    public void beforeConfigure() {
        // create in-memory controllers
        collectionController = new InMemoryCollectionController();
        databaseController = new NoOpDatabaseController();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends CollectionController<?>> DatabaseController<T> getDatabaseController() {
        return (DatabaseController<T>) databaseController;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> CollectionController<T> getCollectionManager() {
        CollectionController<T> cc = (CollectionController<T>) collectionController;
        return cc;
    }

    @Override
    public void configure(Project project) {
        var cmds = project.getCommands();
        cmds.register("hello", HelloCommand.class);
        cmds.register("add", AddEntityCommand.class);
        cmds.register("list", ListEntitiesCommand.class);
    }
}
