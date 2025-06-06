package ru.se.ifmo.lab.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.se.ifmo.lab.ResponseWrapper;
import ru.se.ifmo.lab.model.LabWork;

public class UpdateIdCommand implements Command<LabWork, Void> {
    @Override
    public String name() {
        return "UPDATE_ID";
    }

    @Override
    public Class<LabWork> requestType() {
        return LabWork.class;
    }

    @Override
    public TypeReference<ResponseWrapper<Void>> responseType() {
        return new TypeReference<>() {
        };
    }
}
