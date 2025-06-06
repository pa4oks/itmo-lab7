package ru.se.ifmo.db;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface TransactionalAction<T> {
    T execute(Connection connection) throws SQLException;
}
