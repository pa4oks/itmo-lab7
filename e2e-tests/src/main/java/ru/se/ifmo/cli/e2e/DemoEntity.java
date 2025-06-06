package ru.se.ifmo.cli.e2e;

import ru.se.ifmo.db.entity.Entity;

public class DemoEntity implements Entity {
    private long id;
    private final String name;

    public DemoEntity(String name) {
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}

