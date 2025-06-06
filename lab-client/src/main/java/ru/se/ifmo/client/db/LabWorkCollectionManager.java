package ru.se.ifmo.client.db;

import ru.se.ifmo.client.NioEndpointClient;
import ru.se.ifmo.db.CollectionManager;
import ru.se.ifmo.lab.ResponseWrapper;
import ru.se.ifmo.lab.dto.CollectionInfo;
import ru.se.ifmo.lab.endpoint.AddCommand;
import ru.se.ifmo.lab.endpoint.ClearCommand;
import ru.se.ifmo.lab.endpoint.InfoCommand;
import ru.se.ifmo.lab.endpoint.RemoveByIdCommand;
import ru.se.ifmo.lab.endpoint.UpdateIdCommand;
import ru.se.ifmo.lab.model.LabWork;

import java.time.LocalDate;
import java.util.Collection;
import java.util.PriorityQueue;

public class LabWorkCollectionManager implements CollectionManager<LabWork> {
    private final NioEndpointClient client;
    private final PriorityQueue<LabWork> queue = new PriorityQueue<>();
    private LocalDate initDate;

    public LabWorkCollectionManager(NioEndpointClient client) {
        this.client = client;
    }

    @Override
    public LocalDate getInitializeDate() {
        if (initDate == null) {
            ResponseWrapper<CollectionInfo> r = client.execute(new InfoCommand(), null);
            if (!r.success()) throw new IllegalStateException(r.message());
            initDate = r.data().getInitDate();
        }
        return initDate;
    }

    @Override
    public long add(LabWork entity) {
        queue.add(entity);
        ResponseWrapper<Long> r = client.execute(new AddCommand(), entity);
        if (!r.success()) throw new IllegalStateException(r.message());
        entity.setId(r.data());
        return r.data();
    }

    @Override
    public void remove(LabWork entity) {
        queue.remove(entity);
        ResponseWrapper<Void> r = client.execute(new RemoveByIdCommand(), entity.getId());
        if (!r.success()) throw new IllegalStateException(r.message());
    }

    @Override
    public LabWork get(long id) {
        return queue.stream().filter(lw -> lw.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void update(long id, LabWork entity) {
        entity.setId(id);
        queue.removeIf(lw -> lw.getId() == id);
        queue.add(entity);
        ResponseWrapper<Void> r = client.execute(new UpdateIdCommand(), entity);
        if (!r.success()) throw new IllegalStateException(r.message());
    }

    @Override
    public Collection<LabWork> getCollection() {
        return queue;
    }

    @Override
    public void clear() {
        queue.clear();
        ResponseWrapper<Void> r = client.execute(new ClearCommand(), null);
        if (!r.success()) throw new IllegalStateException(r.message());
    }

    @Override
    public void addAll(Collection<LabWork> entities) {
        queue.addAll(entities);
    }
}
