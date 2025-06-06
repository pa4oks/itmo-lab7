package ru.se.ifmo.lab.server.endpoint;

import ru.se.ifmo.lab.dto.CollectionInfo;
import ru.se.ifmo.lab.endpoint.InfoCommand;
import ru.se.ifmo.lab.model.LabWork;
import ru.se.ifmo.lab.server.GenericEndpoint;
import ru.se.ifmo.lab.server.db.LabWorkCollectionManager;

public class InfoEndpoint extends GenericEndpoint<Void, CollectionInfo> {
    public InfoEndpoint(LabWorkCollectionManager manager) {
        super(new InfoCommand(), unused -> {
            CollectionInfo info = new CollectionInfo();
            info.setInitDate(manager.getInitializeDate());
            info.setType(LabWork.class.getSimpleName());
            info.setSize(manager.getCollection().size());
            return info;
        });
    }
}
