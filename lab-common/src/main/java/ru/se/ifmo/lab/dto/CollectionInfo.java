package ru.se.ifmo.lab.dto;

import java.time.LocalDate;
import java.util.Objects;

public class CollectionInfo {
    private String type;
    private LocalDate initDate;
    private int size;

    public CollectionInfo() {}

    public CollectionInfo(String type, LocalDate initDate, int size) {
        this.type = type;
        this.initDate = initDate;
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public int getSize() {
        return size;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "CollectionInfo{" +
                "type='" + type + '\'' +
                ", initDate=" + initDate +
                ", size=" + size +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CollectionInfo that)) return false;
        return size == that.size && Objects.equals(type, that.type) && Objects.equals(initDate, that.initDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, initDate, size);
    }
}
