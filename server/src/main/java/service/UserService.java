package service;

import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.UserData;
import model.AuthData;
import java.util.UUID;

public class UserService{
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

public UserService (UserDAO userDAO, AuthDAO authDAO) {
    this.userDAO = userDAO;
    this.authDAO = authDAO;

}
    public record RegisterRequest(String username, String password, String email){}
    public record RegisterResult()
    public record LoginRequest()
    public record LoginResult

/*
create user data
saveuser data to database
call userdao.createuser(user)

create token using the uuid random to string

create/store auth data

return relust
 */

    public LoginResult login(LoginRequest userreq) throws DataAccessException {
        if (userreq.username == null || userreq.password == null) {
            throw new DataAccessException("Error: bad request");
        }

        /*
        get user from database
        veryfiy user exists
        generate auth token
        create + store auth data
        return reuslt
         */
    }

    public void logout(String authToken) throws DataAccessException {
        authDAO.deleteAuth(authToken);
    }

    public void clear() throws DataAccessException{
        userDAO.clear();
        authDAO.clear();

    }

}



