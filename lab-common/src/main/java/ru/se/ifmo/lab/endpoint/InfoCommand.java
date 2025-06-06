package ru.se.ifmo.lab.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.se.ifmo.lab.ResponseWrapper;
import ru.se.ifmo.lab.dto.CollectionInfo;

public class InfoCommand implements Command<Void, CollectionInfo> {
    @Override
    public String name() {
        return "INFO";
    }

    @Override
    public Class<Void> requestType() {
        return Void.class;
    }

    @Override
    public TypeReference<ResponseWrapper<CollectionInfo>> responseType() {
        return new TypeReference<>() {
        };
    }
}
