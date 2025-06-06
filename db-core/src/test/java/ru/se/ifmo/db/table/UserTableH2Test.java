package ru.se.ifmo.db.table;

import org.junit.jupiter.api.*;
import ru.se.ifmo.db.entity.User;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class UserTableH2Test {

    private static Connection connection;
    private UserTable userTable;

    @BeforeAll
    static void initDatabase() throws SQLException {
        // create an in-memory H2 database that lives as long as the VM
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE users (" +
                "username VARCHAR(255) PRIMARY KEY, " +
                "password VARCHAR(255) NOT NULL)");
        }
    }

    @AfterAll
    static void shutdownDatabase() throws SQLException {
        connection.close();
    }

    @BeforeEach
    void setUp() throws SQLException {
        // clear out any leftover rows
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM users");
        }
        userTable = new UserTable(connection);
    }

    @Test
    void insertUser_andIsExist_andGetUser_success() {
        User alice = new User();
        alice.setUsername("alice");
        alice.setPassword("pass1");
        User bob   = new User();
        bob.setUsername("bob");
        bob.setPassword("pass2");

        assertFalse(userTable.isExist("alice"));
        assertFalse(userTable.isExist("bob"));

        userTable.insertUser(alice);
        assertTrue(userTable.isExist("alice"));
        assertFalse(userTable.isExist("bob"));

        userTable.insertUser(bob);
        assertTrue(userTable.isExist("bob"));

        User fetched = userTable.getUser("alice");
        assertNotNull(fetched);
        assertEquals("alice", fetched.getUsername());
        assertEquals("pass1", fetched.getPassword());
    }

    @Test
    void isExist_nonExistingUser_returnsFalse() {
        assertFalse(userTable.isExist("charlie"));
    }

    @Test
    void getUser_nonExistingUser_returnsNull() {
        assertNull(userTable.getUser("charlie"));
    }

    @Test
    void insertDuplicate_doesNotThrow_andKeepsSingleRow() throws SQLException {
        User u = new User();
        u.setUsername("dup");
        u.setPassword("pwd");
        userTable.insertUser(u);

        // second insert will violate PK, but insertUser swallows the SQLException
        assertDoesNotThrow(() -> userTable.insertUser(u));

        // still only one row in the table
        try (PreparedStatement ps = connection.prepareStatement(
            "SELECT COUNT(*) FROM users WHERE username = ?")) {
            ps.setString(1, "dup");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(1, rs.getInt(1), "Expected exactly one 'dup' row after duplicate insert");
            }
        }
    }
}
