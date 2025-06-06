package ru.se.ifmo.lab.server.endpoint;

import ru.se.ifmo.lab.endpoint.ClearCommand;
import ru.se.ifmo.lab.server.GenericEndpoint;
import ru.se.ifmo.lab.server.db.LabWorkCollectionManager;

public class ClearEndpoint extends GenericEndpoint<Void, Void> {
    public ClearEndpoint(LabWorkCollectionManager manager) {
        super(new ClearCommand(), v -> {
            manager.clear();
            return null;
        });
    }
}
