package ru.se.ifmo.lab.server.endpoint;

import ru.se.ifmo.lab.endpoint.RemoveByIdCommand;
import ru.se.ifmo.lab.model.LabWork;
import ru.se.ifmo.lab.server.GenericEndpoint;
import ru.se.ifmo.lab.server.db.LabWorkCollectionManager;

public class RemoveEndpoint extends GenericEndpoint<Long, Void> {
    public RemoveEndpoint(LabWorkCollectionManager manager) {
        super(new RemoveByIdCommand(), id -> {
            var coll = manager.getCollection();
            coll.stream()
                    .map(e -> (LabWork) e)
                    .filter(e -> e.getId() == id)
                    .findFirst().ifPresent(manager::remove);
            return null;
        });
    }
}
