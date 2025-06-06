package ru.se.ifmo.lab.model;

import java.util.Objects;

public class Coordinates {
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        if (x <= -204) {
            throw new IllegalArgumentException("X out of range: " + x);
        }
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates that)) return false;
        return Float.compare(that.x, x) == 0 &&
                Float.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
