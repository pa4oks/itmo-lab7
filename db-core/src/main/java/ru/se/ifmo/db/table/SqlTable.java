package ru.se.ifmo.db.table;

import ru.se.ifmo.db.DatabaseUtils;
import ru.se.ifmo.db.TransactionalAction;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class SqlTable {
    private final Connection connection;

    public SqlTable(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public <T> T inTransaction(TransactionalAction<T> action) {
        boolean originalAutoCommit = true;
        try {
            originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            T result = action.execute(connection);
            connection.commit();
            return result;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                DatabaseUtils.logSQLException(rollbackEx);
            }
            DatabaseUtils.logSQLException(e);
            return null;
        } finally {
            try {
                connection.setAutoCommit(originalAutoCommit);
            } catch (SQLException e) {
                DatabaseUtils.logSQLException(e);
            }
        }
    }
}
