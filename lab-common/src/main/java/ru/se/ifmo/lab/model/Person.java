package ru.se.ifmo.lab.model;

import java.util.Objects;

public class Person {
    private String name;
    private int weight;
    private Color eyeColor;
    private Color hairColor;
    private Country nationality;
    private Location location;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("weight must be greater than 0");
        }
        this.weight = weight;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        if (eyeColor == null) {
            throw new IllegalArgumentException("eyeColor cannot be null");
        }
        this.eyeColor = eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", eyeColor=" + eyeColor +
                ", hairColor=" + hairColor +
                ", nationality=" + nationality +
                ", location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Person)) return false;
        Person p = (Person) o;
        return weight == p.weight &&
                Objects.equals(name, p.name) &&
                eyeColor == p.eyeColor &&
                hairColor == p.hairColor &&
                nationality == p.nationality &&
                Objects.equals(location, p.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight, eyeColor, hairColor, nationality, location);
    }
}

