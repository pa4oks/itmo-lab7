package ru.se.ifmo.lab.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.se.ifmo.lab.ResponseWrapper;

import java.util.List;

public class HelpCommand implements Command<Void, List<String>> {
    @Override
    public String name() {
        return "HELP";
    }

    @Override
    public Class<Void> requestType() {
        return Void.class;
    }

    @Override
    public TypeReference<ResponseWrapper<List<String>>> responseType() {
        return new TypeReference<>() {
        };
    }
}
