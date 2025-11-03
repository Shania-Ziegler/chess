package dataaccess;

import model.UserData;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt; //pass hashing

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException{
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()) { //conn is connection object
            var createTableSQL =
                    """
                            CREATE TABLE IF NOT EXISTS users (
                            username VARCHAR(255) NOT NULL PRIMARY KEY,
                            password VARCHAR(255) NOT NULL,
                            email VARCHAR(255) NOT NULL )""";
            try (var stmt = conn.prepareStatement(createTableSQL)) { //stm prepared statement objecct
                stmt.executeUpdate();
            }
        }catch (SQLException e){
                throw new DataAccessException("Unable to configure database:" + e.getMessage());
            }
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var sql = "INSERT INTO users(username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(sql)) {
                //hash pass before store
                String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());

                stmt.setString(1, user.username());
                stmt.setString(2, hashedPassword);
                stmt.setString(3, user.email());

                stmt.executeUpdate();

            }
            }catch (SQLException e) {
            throw new DataAccessException("Error creating user: " + e.getMessage());
        }
    }


    @Override
    public UserData getUser(String username) throwsDataAccessException {
        throw new DataAccessException("To be implemented");
    }
    public void clear() throwsDataAccessException{
        throw new DataAccessException("To be implemented");
    }

}
