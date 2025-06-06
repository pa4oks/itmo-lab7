package ru.se.ifmo.lab.server.endpoint;

import ru.se.ifmo.lab.endpoint.ShowCommand;
import ru.se.ifmo.lab.model.LabWork;
import ru.se.ifmo.lab.server.GenericEndpoint;
import ru.se.ifmo.lab.server.db.LabWorkCollectionManager;

import java.util.List;

public class ShowEndpoint extends GenericEndpoint<Void, List<LabWork>> {
    public ShowEndpoint(LabWorkCollectionManager manager) {
        super(new ShowCommand(), lw -> manager.getCollection().stream().toList());
    }
}
