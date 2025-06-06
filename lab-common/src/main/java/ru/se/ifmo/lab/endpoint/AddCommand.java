package ru.se.ifmo.lab.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.se.ifmo.lab.ResponseWrapper;
import ru.se.ifmo.lab.model.LabWork;

public class AddCommand implements Command<LabWork, Long> {
    @Override
    public String name() {
        return "ADD";
    }

    @Override
    public Class<LabWork> requestType() {
        return LabWork.class;
    }

    @Override
    public TypeReference<ResponseWrapper<Long>> responseType() {
        return new TypeReference<>() {
        };
    }
}
