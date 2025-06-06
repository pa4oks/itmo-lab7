package ru.se.ifmo.lab.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.se.ifmo.lab.ResponseWrapper;

public class RemoveLowerCommand implements Command<Long, Void> {
    @Override
    public String name() {
        return "REMOVE_LOWER";
    }

    @Override
    public Class<Long> requestType() {
        return Long.class;
    }

    @Override
    public TypeReference<ResponseWrapper<Void>> responseType() {
        return new TypeReference<>() {
        };
    }
}
