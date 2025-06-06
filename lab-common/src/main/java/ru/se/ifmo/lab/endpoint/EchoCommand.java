package ru.se.ifmo.lab.endpoint;


import com.fasterxml.jackson.core.type.TypeReference;
import ru.se.ifmo.lab.ResponseWrapper;

public class EchoCommand implements Command<String, String> {

    @Override
    public String name() {
        return "ECHO";
    }

    @Override
    public Class<String> requestType() {
        return String.class;
    }

    @Override
    public TypeReference<ResponseWrapper<String>> responseType() {
        return new TypeReference<>() {
        };
    }
}

