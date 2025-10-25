package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear() throws DataAccesssException;
    void createAuth(AuthDatam, auth) throws DataAccesssException;;
    AuthData getAuth(String authToken) throws DataAccesssException;;
    void deleteAuth(String authToken) throws DataAccesssException;
}
