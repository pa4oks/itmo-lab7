// server/src/main/java/ru/se/ifmo/lab/server/Endpoint.java
package ru.se.ifmo.lab.server;

import ru.se.ifmo.lab.endpoint.Command;
import java.io.OutputStream;
import java.io.IOException;

public interface Endpoint<Req,Res> {
    Command<Req,Res> getCommand();
    void handle(String payload, OutputStream out) throws IOException;
}
