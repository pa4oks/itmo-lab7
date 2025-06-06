package ru.se.ifmo.lab.server;

import java.util.Map;
import java.util.HashMap;

public class Router {
    private final Map<String, Endpoint<?,?>> routes = new HashMap<>();

    public void register(Endpoint<?,?> ep) {
        routes.put(ep.getCommand().name(), ep);
    }

    public Endpoint<?,?> find(String cmd) {
        return routes.get(cmd);
    }
}
