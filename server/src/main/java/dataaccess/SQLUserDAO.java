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
    public void createUser(UserData user) throwsDataAccessException{
        throw new DataAccessException("To be implemented");
    }
    @Override
    public UserData getUser(String username) throwsDataAccessException {
        throw new DataAccessException("To be implemented");
    }
    public void clear() throwsDataAccessException{
        throw new DataAccessException("To be implemented");
    }

}
