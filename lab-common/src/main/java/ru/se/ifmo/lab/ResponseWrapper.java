package ru.se.ifmo.lab;

public record ResponseWrapper<T>(boolean success, String message, T data) {

    public static <T> ResponseWrapper<T> success(T data) {
        return new ResponseWrapper<>(true, "", data);
    }

    public static <T> ResponseWrapper<T> error(String message) {
        return new ResponseWrapper<>(false, message, null);
    }
}
