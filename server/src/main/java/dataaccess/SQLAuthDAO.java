package dataaccess;

import model.AuthData;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var createTableSQL = """
                CREATE TABLE IF NOT EXISTS auth (
                    authToken VARCHAR(255) NOT NULL PRIMARY KEY,
                    username VARCHAR(255) NOT NULL)""";
            try (var stmt = conn.prepareStatement(createTableSQL)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to configure database: " + e.getMessage());
        }
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        var sql = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, auth.authToken());
                stmt.setString(2, auth.username());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating auth: " + e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var sql = "SELECT authToken, username FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, authToken);

                try (var rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String token = rs.getString("authToken");
                        String username = rs.getString("username");
                        return new AuthData(token, username);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting auth: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var sql = "DELETE FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, authToken);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting auth: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var sql = "DELETE FROM auth";
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing auth: " + e.getMessage());
        }
    }
}