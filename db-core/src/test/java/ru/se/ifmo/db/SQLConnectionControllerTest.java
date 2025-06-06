package ru.se.ifmo.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class SQLConnectionControllerTest {

    private Driver successDriver;
    private Driver failureDriver;

    @AfterEach
    void tearDown() throws SQLException {
        if (successDriver != null) DriverManager.deregisterDriver(successDriver);
        if (failureDriver != null) DriverManager.deregisterDriver(failureDriver);
    }

    @Test
    void getConnection_returnsConnectionFromDriver() throws SQLException {
        Connection expected = mock(Connection.class);
        successDriver = new Driver() {
            @Override
            public Connection connect(String url, Properties info) {
                return expected;
            }
            @Override public boolean acceptsURL(String url) { return url.startsWith("jdbc:dummy"); }
            @Override public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) { return new DriverPropertyInfo[0]; }
            @Override public int getMajorVersion() { return 1; }
            @Override public int getMinorVersion() { return 0; }
            @Override public boolean jdbcCompliant() { return false; }
            @Override public java.util.logging.Logger getParentLogger() { throw new UnsupportedOperationException(); }
        };
        DriverManager.registerDriver(successDriver);
        System.setProperty("db.driver", "dummy");
        System.setProperty("db.host", "host");
        System.setProperty("db.port", "1234");
        System.setProperty("db.name", "db");
        System.setProperty("db.user", "user");
        System.setProperty("db.password", "pass");
        SQLConnectionController controller = new SQLConnectionController();
        assertSame(expected, controller.getConnection());
    }

    @Test
    void getConnection_returnsNullOnSQLException() throws SQLException {
        failureDriver = new Driver() {
            @Override
            public Connection connect(String url, Properties info) throws SQLException {
                throw new SQLException();
            }
            @Override public boolean acceptsURL(String url) { return true; }
            @Override public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) { return new DriverPropertyInfo[0]; }
            @Override public int getMajorVersion() { return 1; }
            @Override public int getMinorVersion() { return 0; }
            @Override public boolean jdbcCompliant() { return false; }
            @Override public java.util.logging.Logger getParentLogger() { throw new UnsupportedOperationException(); }
        };
        DriverManager.registerDriver(failureDriver);
        System.setProperty("db.driver", "dummy");
        System.setProperty("db.host", "host");
        System.setProperty("db.port", "1234");
        System.setProperty("db.name", "db");
        System.setProperty("db.user", "user");
        System.setProperty("db.password", "pass");
        SQLConnectionController controller = new SQLConnectionController();
        assertNull(controller.getConnection());
    }

    // helper mock
    private Connection mock(Class<Connection> cls) {
        return org.mockito.Mockito.mock(cls);
    }
}
