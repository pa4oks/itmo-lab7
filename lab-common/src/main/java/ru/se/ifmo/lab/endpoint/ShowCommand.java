package ru.se.ifmo.lab.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.se.ifmo.lab.ResponseWrapper;
import ru.se.ifmo.lab.model.LabWork;

import java.util.List;

public class ShowCommand implements Command<Void, List<LabWork>> {
    @Override
    public String name() {
        return "SHOW";
    }

    @Override
    public Class<Void> requestType() {
        return Void.class;
    }

    @Override
    public TypeReference<ResponseWrapper<List<LabWork>>> responseType() {
        return new TypeReference<>() {};
    }
}
