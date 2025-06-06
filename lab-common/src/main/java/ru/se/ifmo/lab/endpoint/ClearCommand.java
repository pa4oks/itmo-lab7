package ru.se.ifmo.lab.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.se.ifmo.lab.ResponseWrapper;

public class ClearCommand implements Command<Void, Void> {
    @Override
    public String name() {
        return "CLEAR";
    }

    @Override
    public Class<Void> requestType() {
        return Void.class;
    }

    @Override
    public TypeReference<ResponseWrapper<Void>> responseType() {
        return new TypeReference<>() {};
    }
}
