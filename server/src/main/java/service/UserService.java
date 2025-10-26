package service;

import dataaccess.UserDAO;
import model.UserData;
import java.util.UUID;

public class UserService{
    private final UserDAO;
    private final AuthDAO;

public UserService (UserDAO userDAO, AuthDAO authDAO) {
    this.userDAO = UserDAO;
    this.authDAO = authDAO;

}
    //request and result record

//register

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
        if (authToken == null) {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void clear() throws DataAccessException{

    }

}



