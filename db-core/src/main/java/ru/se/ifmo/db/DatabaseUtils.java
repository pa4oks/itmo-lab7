package ru.se.ifmo.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Utility methods for handling SQLExceptions and executing simple JDBC statements.
 *
 * <p>All methods are static. This class can't be instantiated.</p>
 */
public final class DatabaseUtils {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);

    private DatabaseUtils() {
    }

    /**
     * Logs details of the given {@link SQLException} and any nested exceptions.
     *
     * @param sqlException the exception whose details are to be logged
     */
    public static void logSQLException(SQLException sqlException) {
        for (Throwable e : sqlException) {
            if (e instanceof SQLException se) {
                logger.error(
                    "SQL error; SQLState={}, ErrorCode={}, Message={}",
                    se.getSQLState(),
                    se.getErrorCode(),
                    se.getMessage(),
                    se
                );
            }
        }
    }
}
