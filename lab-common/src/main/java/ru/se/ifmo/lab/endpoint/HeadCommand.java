package ru.se.ifmo.lab.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.se.ifmo.lab.ResponseWrapper;
import ru.se.ifmo.lab.model.LabWork;

public class HeadCommand implements Command<Void, LabWork> {
    @Override
    public String name() {
        return "HEAD";
    }

    @Override
    public Class<Void> requestType() {
        return Void.class;
    }

    @Override
    public TypeReference<ResponseWrapper<LabWork>> responseType() {
        return new TypeReference<>() {
        };
    }
}
