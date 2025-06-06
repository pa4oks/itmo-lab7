package ru.se.ifmo.lab.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.lab.ResponseWrapper;
import ru.se.ifmo.lab.endpoint.Command;
import ru.se.ifmo.lab.util.JacksonUtil;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class GenericEndpoint<Req, Res> implements Endpoint<Req, Res> {
    private static final Logger logger = LoggerFactory.getLogger(GenericEndpoint.class);
    private static final ObjectMapper mapper = JacksonUtil.createMapper();

    private final Command<Req, Res> command;
    private final Function<Req, Res> handler;

    public GenericEndpoint(Command<Req, Res> command, Function<Req, Res> handler) {
        this.command = command;
        this.handler = handler;
    }

    @Override
    public Command<Req, Res> getCommand() {
        return command;
    }

    @Override
    public void handle(String payload, OutputStream out) {
        logger.info("Handling {} with payload [{}]", command.name(), payload);

        ResponseWrapper<Res> response;

        try {
            Req req;
            try {
                if (command.requestType() == Void.class) {
                    req = null;
                } else {
                    req = mapper.readValue(payload, command.requestType());
                }
            } catch (Exception e) {
                logger.warn("Failed to parse or handle {}: {}", command.name(), e.toString());
                writeJson(out, ResponseWrapper.error("Invalid payload or internal error: " + e.getMessage()));
                return;
            }


            Res res;
            try {
                res = handler.apply(req);
            } catch (Exception e) {
                logger.error("Handler failed for {}: {}", command.name(), e.toString(), e);
                response = ResponseWrapper.error("Execution error: " + e.getMessage());
                writeJson(out, response);
                return;
            }

            logger.info("Responding {} -> [{}]", command.name(), res);
            response = ResponseWrapper.success(res);

        } catch (Exception e) {
            logger.warn("Failed to parse or handle {}: {}", command.name(), e.toString(), e);
            response = ResponseWrapper.error("Invalid payload or internal error: " + e.getMessage());
        }

        try {
            writeJson(out, response);
        } catch (IOException io) {
            logger.error("Failed to send response for {}: {}", command.name(), io, io);
        }
    }

    private void writeJson(OutputStream out, ResponseWrapper<?> response) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        String json = mapper.writeValueAsString(response);
        writer.write(json);
        writer.newLine();
        writer.flush();
    }
}
