package ru.se.ifmo.lab.model;

import java.util.Arrays;

public enum Difficulty {
    EASY,
    NORMAL,
    HARD,
    VERY_HARD,
    HOPELESS;

    public static String[] names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .toArray(String[]::new);
    }
}