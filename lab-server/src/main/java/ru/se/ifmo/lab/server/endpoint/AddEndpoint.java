package ru.se.ifmo.lab.server.endpoint;

import ru.se.ifmo.lab.endpoint.AddCommand;
import ru.se.ifmo.lab.model.LabWork;
import ru.se.ifmo.lab.server.GenericEndpoint;
import ru.se.ifmo.lab.server.db.LabWorkCollectionManager;
import ru.se.ifmo.lab.server.db.LabWorkDatabaseManager;


public class AddEndpoint extends GenericEndpoint<LabWork, Long> {

    public AddEndpoint(LabWorkCollectionManager manager, LabWorkDatabaseManager databaseManager) {
        super(new AddCommand(), lw -> {
            if (lw.getId() > 0) {
                throw new IllegalArgumentException("cannot add entity with preset id");
            }
            long id = manager.add(lw);
            if (!databaseManager.save(manager)) {
                throw new IllegalStateException("Failed to persist LabWork collection to CSV");
            }
            return id;
        });
    }
}

