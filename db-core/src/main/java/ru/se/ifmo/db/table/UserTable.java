package ru.se.ifmo.db.table;

import jakarta.inject.Inject;
import ru.se.ifmo.db.DatabaseUtils;
import ru.se.ifmo.db.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class UserTable extends SqlTable {

    @Inject
    public UserTable(Connection connection) {
        super(connection);
    }

    public void insertUser(User user) {
        String sql = "insert into users (username, password) values (?, ?)";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            DatabaseUtils.logSQLException(e);
        }
    }

    public boolean isExist(String username) {
        String sql = "select id from users where username = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            DatabaseUtils.logSQLException(e);
        }
        return false;
    }

    public User getUser(String username) {
        String sql = "select * from users where username = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                User user = new User();
                user.setId(id);
                user.setUsername(username);
                user.setPassword(password);
                return user;
            }
        } catch (SQLException e) {
            DatabaseUtils.logSQLException(e);
        }
        return null;
    }

    public User getById(long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }
        } catch (SQLException e) {
            DatabaseUtils.logSQLException(e);
        }
        return null;
    }

}
