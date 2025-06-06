package ru.se.ifmo.client;

import ru.se.ifmo.lab.ResponseWrapper;
import ru.se.ifmo.lab.endpoint.Command;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.lab.util.JacksonUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@SuppressWarnings("BusyWait")
public class NioEndpointClient implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(NioEndpointClient.class);
    private final SocketChannel channel;
    private final ObjectMapper mapper = JacksonUtil.createMapper();

    // google.com -> dns -> ip:port
    public NioEndpointClient(String host, int port) {
        Objects.requireNonNull(host, "host");
        SocketChannel ch = null;
        while (true) {
            try {
                ch = SocketChannel.open();
                ch.configureBlocking(false);
                ch.connect(new InetSocketAddress(host, port));
                while (!ch.finishConnect()) {
                    Thread.sleep(100);
                }
                logger.info("Connected to {}:{}", host, port);
                break;
            } catch (Exception e) {
                logger.warn("Connection failed, retrying in 5s", e);
                closeQuietly(ch);
                sleep();
            }
        }
        this.channel = ch;
    }

    public <Req, Res> ResponseWrapper<Res> execute(Command<Req, Res> command, Req request) {
        try {
            ObjectNode obj = JacksonUtil.createMapper().createObjectNode();
            obj.put("command", command.name());
            obj.set("payload", mapper.valueToTree(request));
            String jsonReq = mapper.writeValueAsString(obj) + "\n";

            ByteBuffer wb = ByteBuffer.wrap(jsonReq.getBytes(StandardCharsets.UTF_8));
            while (wb.hasRemaining()) {
                channel.write(wb);
            }
            logger.info("Sent JSON request for {}", command.name());

            ByteBuffer rb = ByteBuffer.allocate(8192);
            int r;
            do {
                r = channel.read(rb);
            } while (r == 0);

            if (r == -1) {
                logger.error("Server closed connection unexpectedly");
                closeQuietly(channel);
                return null;
            }

            rb.flip();
            String jsonResp = StandardCharsets.UTF_8.decode(rb).toString().trim();
            logger.info("Received JSON response for {}: {}", command.name(), jsonResp);

            return mapper.readValue(jsonResp, command.responseType());

        } catch (Exception e) {
            logger.error("Error during {} execution", command.name(), e);
            return null;
        }
    }


    @Override
    public void close() {
        closeQuietly(channel);
        logger.info("Connection closed");
    }

    private void closeQuietly(SocketChannel ch) {
        if (ch != null) {
            try { ch.close(); }
            catch (Exception e) { logger.warn("Failed to close channel", e); }
        }
    }

    private void sleep() {
        try { Thread.sleep(5000); }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            logger.warn("Sleep interrupted", ie);
        }
    }
}