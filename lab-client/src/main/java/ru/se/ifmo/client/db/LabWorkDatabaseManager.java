package ru.se.ifmo.client.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.client.NioEndpointClient;
import ru.se.ifmo.db.DatabaseManager;
import ru.se.ifmo.lab.ResponseWrapper;
import ru.se.ifmo.lab.endpoint.ShowCommand;
import ru.se.ifmo.lab.model.LabWork;

import java.util.List;

public class LabWorkDatabaseManager implements DatabaseManager<LabWorkCollectionManager> {
    private static final Logger logger = LoggerFactory.getLogger(LabWorkDatabaseManager.class);
    private final NioEndpointClient client;

    public LabWorkDatabaseManager(NioEndpointClient client) {
        this.client = client;
    }

    @Override
    public boolean load(LabWorkCollectionManager collectionController) {
        try {
            ResponseWrapper<List<LabWork>> resp = client.execute(new ShowCommand(), null);
            if (!resp.success()) {
                logger.error("Failed to load: {}", resp.message());
                return false;
            }
            collectionController.addAll(resp.data());
            return true;
        } catch (Exception e) {
            logger.error("Exception during load", e);
            return false;
        }
    }

    @Override
    public boolean save(LabWorkCollectionManager collectionController) {
        throw new IllegalStateException("Client cannot be saved to the database");
    }
}
