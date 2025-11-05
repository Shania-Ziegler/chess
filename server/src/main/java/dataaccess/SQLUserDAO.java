package dataaccess;

import model.UserData;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var createTableSQL =
                    """
                            CREATE TABLE IF NOT EXISTS users (
                            username VARCHAR(255) NOT NULL PRIMARY KEY,
                            password VARCHAR(255) NOT NULL,
                            email VARCHAR(255) NOT NULL )""";
            try (var stmt = conn.prepareStatement(createTableSQL)) { //stm prepared statement objecct
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to configure database:" + e.getMessage());
        }
    }



    @Override
    public void createUser(UserData user) throws DataAccessException {
        var sql = "INSERT INTO users(username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(sql)) {

                String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());

                stmt.setString(1, user.username());
                stmt.setString(2, hashedPassword);
                stmt.setString(3, user.email());

                stmt.executeUpdate();

            }

        }catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")){
            throw new DataAccessException("Error already taken");
            }
            throw new DataAccessException("Error creating user: " + e.getMessage());
        }
    }


    @Override
    public UserData getUser(String username) throws DataAccessException {
        var sql = "SELECT username, password, email FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);

            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String user = rs.getString("username");
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(user, password, email);
                }
            }
        }
    }catch (SQLException e){
        throw new DataAccessException("Error getting user: " + e.getMessage());
        }
        return null;
        }

    @Override
    public void clear() throws DataAccessException {
        var sql = "DELETE FROM users";
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(sql)){
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            try {
                throw new DataAccessException("Error clearing users:" + e.getMessage()) {


                };
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }

        }

    }
}



