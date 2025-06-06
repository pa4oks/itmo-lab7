package ru.se.ifmo.cli.e2e;

import ru.se.ifmo.db.CollectionController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InMemoryCollectionController implements CollectionController<DemoEntity> {
    private final LocalDate init = LocalDate.now();
    private final List<DemoEntity> data = new ArrayList<>();
    private long nextId = 1;

    @Override
    public LocalDate getInitializeDate() {
        return init;
    }

    @Override
    public void add(DemoEntity e) {
        e.setId(nextId++);
        data.add(e);
    }

    @Override
    public void remove(DemoEntity e) {
        data.remove(e);
    }

    @Override
    public DemoEntity get(long id) {
        return data.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void update(long id, DemoEntity e) {
        remove(get(id));
        e.setId(id);
        data.add(e);
    }

    @Override
    public Collection<DemoEntity> getCollection() {
        return List.copyOf(data);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public void addAll(Collection<DemoEntity> c) {
        data.addAll(c);
    }

    @Override
    public ru.se.ifmo.db.entity.User getOwner() {
        return null;
    }
}
