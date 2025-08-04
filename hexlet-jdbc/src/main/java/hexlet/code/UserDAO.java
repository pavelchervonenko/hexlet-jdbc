package hexlet.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection conn) {
        connection = conn;
    }

    public void createTable() throws SQLException {
        var sql = "CREATE TABLE users ("
                + "id BIGINT PRIMARY KEY AUTO_INCREMENT,"
                + "username VARCHAR(255),"
                + "phone VARCHAR(255))";

        try (var stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void save(User user) throws SQLException {
        if (user.getId() == null) {
            var sql = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPhone());
                stmt.executeUpdate();

                var generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        } else {
            // код обновления записи
            var sql = "UPDATE users SET username = ?, phone = ? WHERE id = ?";
            try (var stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPhone());
                stmt.setLong(3, user.getId());
                stmt.executeUpdate();
            }
        }
    }

    public Optional<User> find(Long id) throws SQLException {
        var sql = "SELECT * FROM users WHERE id = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                var username = resultSet.getString("username");
                var phone = resultSet.getString("phone");

                var user = new User(username, phone);
                user.setId(id);
                return Optional.of(user);
            }
            return Optional.empty();
        }
    }

    public void deleteById(Long id) throws SQLException {
        if (id != null) {
            var sql = "DELETE FROM users WHERE id = ?";
            try (var stmt = connection.prepareStatement(sql)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        } else {
            throw new SQLException("Id not found");
        }
    }
}