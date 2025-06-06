package ru.se.ifmo.lab.server.db;

import ru.se.ifmo.db.CollectionManager;
import ru.se.ifmo.lab.model.LabWork;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Random;

public class LabWorkCollectionManager implements CollectionManager<LabWork> {
    private static final Random random = new Random();
    private final PriorityQueue<LabWork> queue = new PriorityQueue<>();
    private final LocalDate initDate = LocalDate.now();

    private long generateNextId() {
        return Math.abs(random.nextLong());
    }

    @Override
    public LocalDate getInitializeDate() {
        return initDate;
    }

    @Override
    public long add(LabWork entity) {
        if (entity.getId() <= 0) {
            entity.setId(generateNextId());
        }
        queue.add(entity);
        return entity.getId();
    }

    @Override
    public void remove(LabWork entity) {
        queue.remove(entity);
    }

    @Override
    public LabWork get(long id) {
        return queue.stream()
                .filter(lw -> lw.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(long id, LabWork entity) {
        queue.removeIf(lw -> lw.getId() == id);
        entity.setId(id);
        queue.add(entity);
    }

    @Override
    public Collection<LabWork> getCollection() {
        return new ArrayList<>(queue);
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public void addAll(Collection<LabWork> entities) {
        entities.forEach(this::add);
    }
}
