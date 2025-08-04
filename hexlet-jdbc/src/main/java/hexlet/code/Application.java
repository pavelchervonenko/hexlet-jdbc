package hexlet.code;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    public static void main(String[] args) throws SQLException {
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {

            var sqlCreate = "CREATE TABLE users ("
                    + "id BIGINT PRIMARY KEY AUTO_INCREMENT,"
                    + "username VARCHAR(255),"
                    + "phone VARCHAR(255))";

            try (var statement = conn.createStatement()) {
                statement.execute(sqlCreate);
            }


            var sqlInsertNewUsers = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(sqlInsertNewUsers)) {
                preparedStatement.setString(1, "Tommy");
                preparedStatement.setString(2, "79052466597");
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Ksenia");
                preparedStatement.setString(2, "79022466598");
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Kirill");
                preparedStatement.setString(2, "89067899876");
                preparedStatement.executeUpdate();
            }

            var sqlDelete = "DELETE FROM users WHERE username = ?";
            try (var preparedStatement = conn.prepareStatement(sqlDelete)) {
                preparedStatement.setString(1, "Kirill");
                preparedStatement.executeUpdate();
            }


            var sqlSelect = "SELECT * FROM users";
            try (var statement3 = conn.createStatement()) {
                var resultSet = statement3.executeQuery(sqlSelect);

                while (resultSet.next()) {
                    System.out.println(resultSet.getLong("id"));
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
            }
        }
    }
}