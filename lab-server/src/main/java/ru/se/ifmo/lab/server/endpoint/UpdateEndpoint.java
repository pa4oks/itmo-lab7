package ru.se.ifmo.lab.server.endpoint;

import ru.se.ifmo.lab.endpoint.UpdateIdCommand;
import ru.se.ifmo.lab.model.LabWork;
import ru.se.ifmo.lab.server.GenericEndpoint;
import ru.se.ifmo.lab.server.db.LabWorkCollectionManager;
import ru.se.ifmo.lab.server.db.LabWorkDatabaseManager;

import java.util.List;

public class UpdateEndpoint extends GenericEndpoint<LabWork, Void> {
    public UpdateEndpoint(LabWorkCollectionManager controller, LabWorkDatabaseManager db) {
        super(new UpdateIdCommand(), request -> {
            List<LabWork> current = controller.getCollection().stream()
                    .map(e -> (LabWork) e)
                    .toList();

            LabWork existing = current.stream()
                    .filter(e -> e.getId() == request.getId())
                    .findFirst()
                    .orElse(null);

            if (existing == null) {
                throw new IllegalArgumentException("No LabWork with id = " + request.getId());
            }

            existing.setName(request.getName());
            existing.setCoordinates(request.getCoordinates());
            existing.setCreationDate(request.getCreationDate());
            existing.setMinimalPoint(request.getMinimalPoint());
            existing.setDifficulty(request.getDifficulty());
            existing.setAuthor(request.getAuthor());

            controller.update(existing.getId(), existing);

            if (!db.save(controller)) {
                throw new IllegalStateException("Failed to persist LabWork collection to CSV");
            }

            return null;
        });
    }
}
