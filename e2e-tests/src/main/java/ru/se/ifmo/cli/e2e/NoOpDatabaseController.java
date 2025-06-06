package ru.se.ifmo.cli.e2e;

import ru.se.ifmo.db.DatabaseController;

public class NoOpDatabaseController implements DatabaseController<InMemoryCollectionController> {
    @Override
    public boolean load(InMemoryCollectionController c) {
        return true;
    }

    @Override
    public boolean save(InMemoryCollectionController c) {
        return true;
    }
}
