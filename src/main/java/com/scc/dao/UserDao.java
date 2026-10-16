package dao;

import model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HexFormat;
import java.util.Optional;

public class UserDao {

    public boolean register(
            String username,
            String password,
            String role
    ) throws SQLException {

        String sql = """
            INSERT INTO users (username, password, roles, status)
            VALUES (?, ?, ?, 'PENDING')
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, hashPassword(password));
            statement.setString(3, role);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {

            // SQLite error code 19 indicates a constraint violation.
            if (e.getErrorCode() == 19) {
                return false;
            }

            throw e;
        }
    }

    public Optional<User> login(
            String username,
            String password
    ) throws SQLException {

        String sql = """
            SELECT id, username, password, roles, status
            FROM users
            WHERE username = ?
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    String storedPassword = rs.getString("password");
                    String enteredPassword = hashPassword(password);

                    boolean passwordMatches = MessageDigest.isEqual(
                            storedPassword.getBytes(StandardCharsets.UTF_8),
                            enteredPassword.getBytes(StandardCharsets.UTF_8)
                    );

                    if (passwordMatches) {
                        User user = new User(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("roles"),
                                rs.getString("status")
                        );

                        return Optional.of(user);
                    }
                }
            }
        }

        return Optional.empty();
    }

    private String hashPassword(String password) {

        try {
            MessageDigest md =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = md.digest(
                    password.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "Password hashing failed.", e);
        }
    }
}