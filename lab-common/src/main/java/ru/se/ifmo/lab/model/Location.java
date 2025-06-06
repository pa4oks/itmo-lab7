package ru.se.ifmo.lab.model;

import java.util.Objects;

public class Location {
    private long x;
    private Integer y;
    private Float z;
    private String name;

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        if (y == null) {
            throw new IllegalArgumentException("y cannot be null");
        }
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        if (z == null) {
            throw new IllegalArgumentException("z cannot be null");
        }
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Location that)) return false;
        return x == that.x
                && Objects.equals(y, that.y)
                && Objects.equals(z, that.z)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, name);
    }
}
