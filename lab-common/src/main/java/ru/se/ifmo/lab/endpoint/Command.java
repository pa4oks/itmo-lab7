package ru.se.ifmo.lab.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.se.ifmo.lab.ResponseWrapper;

public interface Command<Req, Res> {
    String name();

    Class<Req> requestType();
    TypeReference<ResponseWrapper<Res>> responseType();
}