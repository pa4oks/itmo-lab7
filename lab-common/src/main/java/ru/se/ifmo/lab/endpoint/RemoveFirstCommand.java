package ru.se.ifmo.lab.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.se.ifmo.lab.ResponseWrapper;

public class RemoveFirstCommand implements Command<Void, Void> {
    @Override
    public String name() {
        return "REMOVE_FIRST";
    }

    @Override
    public Class<Void> requestType() {
        return Void.class;
    }

    @Override
    public TypeReference<ResponseWrapper<Void>> responseType() {
        return new TypeReference<>() {
        };
    }
}
