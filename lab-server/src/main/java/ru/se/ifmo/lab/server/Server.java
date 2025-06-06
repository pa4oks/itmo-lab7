package ru.se.ifmo.lab.server;

import ru.se.ifmo.lab.ResponseWrapper;
import ru.se.ifmo.lab.endpoint.EchoCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.lab.server.db.LabWorkCollectionManager;
import ru.se.ifmo.lab.server.db.LabWorkDatabaseManager;
import ru.se.ifmo.lab.server.endpoint.AddEndpoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.se.ifmo.lab.server.endpoint.ClearEndpoint;
import ru.se.ifmo.lab.server.endpoint.InfoEndpoint;
import ru.se.ifmo.lab.server.endpoint.RemoveEndpoint;
import ru.se.ifmo.lab.server.endpoint.ShowEndpoint;
import ru.se.ifmo.lab.server.endpoint.UpdateEndpoint;
import ru.se.ifmo.lab.util.JacksonUtil;

import java.io.ByteArrayOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final ObjectMapper mapper = JacksonUtil.createMapper();

    public static void main(String[] args) throws IOException {
        LabWorkCollectionManager manager = new LabWorkCollectionManager();
        LabWorkDatabaseManager databaseManager = new LabWorkDatabaseManager();
        databaseManager.load(manager);
        Router router = new Router();
        router.register(new GenericEndpoint<>(new EchoCommand(), payload -> payload));
        router.register(new AddEndpoint(manager, databaseManager));
        router.register(new ShowEndpoint(manager));
        router.register(new ClearEndpoint(manager));
        router.register(new InfoEndpoint(manager));
        router.register(new UpdateEndpoint(manager, databaseManager));
        router.register(new RemoveEndpoint(manager));

        int port = 8080;
        try (ServerSocket ss = new ServerSocket(port)) {
            logger.info("Listening on port {}", port);
            while (true) {
                try (Socket sock = ss.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream(), StandardCharsets.UTF_8));
                     OutputStream out = sock.getOutputStream()
                ) {
                    logger.info("Client connected {}", sock.getRemoteSocketAddress());
                    String line;
                    while ((line = reader.readLine()) != null) {
                        ObjectNode reqNode = (ObjectNode) mapper.readTree(line);
                        String cmd = reqNode.get("command").asText();
                        String payload = reqNode.get("payload").isNull()
                                ? ""
                                : reqNode.get("payload").toString();

                        Endpoint<?, ?> ep = router.find(cmd);

                        if (ep != null) {
                            try (ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream()) {
                                ep.handle(payload, buf);
                                out.write(buf.toByteArray());
                                out.flush();
                            }
                        } else {
                            ResponseWrapper<Object> error = ResponseWrapper.error("Unknown command: " + cmd);
                            String jsonResp = mapper.writeValueAsString(error) + "\n";
                            out.write(jsonResp.getBytes(StandardCharsets.UTF_8));
                            out.flush();
                        }
                    }
                } catch (IOException e) {
                    logger.error("Client error", e);
                }
            }
        }
    }
}
