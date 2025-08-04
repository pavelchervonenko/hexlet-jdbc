package hexlet.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class Application {
    public static void main(String[] args) throws SQLException {
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {
            var repo = new UserDAO(conn);
            repo.createTable();

            var u1 = new User("Tommy", "79052345678");
            var u2 = new User("Ksenia", "79056723657");
            var u3 = new User("Kirill", "79052545328");

            repo.save(u1);
            repo.save(u2);
            repo.save(u3);

            repo.deleteById(3L);

            System.out.println("ID u1: " + u1.getId());

            var found1 = repo.find(u1.getId());
            System.out.println(found1);

            var found2 = repo.find(u2.getId());
            System.out.println(found2);

            var found3 = repo.find(u3.getId());
            System.out.println(found3);
        }
    }
}