package ru.se.ifmo.db;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseUtilsTest {

    private final Logger logger = (Logger) LoggerFactory.getLogger(DatabaseUtils.class);
    private final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    void attachAppender() {
        listAppender.start();
        logger.addAppender(listAppender);
    }

    void detachAppender() {
        logger.detachAppender(listAppender);
        listAppender.stop();
    }

    @AfterEach
    void tearDown() {
        detachAppender();
    }

    @Test
    void logSQLException_singleException_logsOneEntry() {
        attachAppender();
        SQLException ex = new SQLException("message1", "state1", 100);
        DatabaseUtils.logSQLException(ex);

        List<ILoggingEvent> logs = listAppender.list;
        assertEquals(1, logs.size());
        String msg = logs.get(0).getFormattedMessage();
        assertTrue(msg.contains("SQLState=state1"));
        assertTrue(msg.contains("ErrorCode=100"));
        assertTrue(msg.contains("Message=message1"));
    }

    @Test
    void logSQLException_chainOfExceptions_logsAllEntries() {
        attachAppender();
        SQLException ex1 = new SQLException("first", "S1", 1);
        SQLException ex2 = new SQLException("second", "S2", 2);
        ex1.setNextException(ex2);

        DatabaseUtils.logSQLException(ex1);

        List<ILoggingEvent> logs = listAppender.list;
        assertEquals(2, logs.size());

        String m1 = logs.get(0).getFormattedMessage();
        assertTrue(m1.contains("SQLState=S1"));
        assertTrue(m1.contains("ErrorCode=1"));
        assertTrue(m1.contains("Message=first"));

        String m2 = logs.get(1).getFormattedMessage();
        assertTrue(m2.contains("SQLState=S2"));
        assertTrue(m2.contains("ErrorCode=2"));
        assertTrue(m2.contains("Message=second"));
    }

    @Test
    void logSQLException_withNonSQLExceptionInChain_skipsNonSQLException() {
        attachAppender();
        SQLException root = new SQLException("root", "RS", 10);
        Exception cause = new Exception("cause");
        root.initCause(cause);

        DatabaseUtils.logSQLException(root);

        List<ILoggingEvent> logs = listAppender.list;
        assertEquals(1, logs.size());
        String msg = logs.get(0).getFormattedMessage();
        assertTrue(msg.contains("SQLState=RS"));
        assertTrue(msg.contains("ErrorCode=10"));
    }
}
