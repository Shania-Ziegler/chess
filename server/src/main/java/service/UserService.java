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
    public record RegisterResult(String username, String authToken){}
    public record LoginRequest(String username, String password){}
    public record LoginResult(String username, String authToken){}

    public RegisterResult register(RegisterRequest req) throws DataAccessException {
        if (req.username() == null || req.password() == null || req.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        UserData user = new UserData(req.username(),req.password(),req.email());
        userDAO.createUser(user);

        //gen auth token
        String authToken = UUID.randomUUID().toString();

        AuthData auth = new AuthData(authToken, req.username());
        authDAO.createAuth(auth);

        return new RegisterResult(req.username(),authToken);
    }


    public LoginResult login(LoginRequest req) throws DataAccessException {
        if (req.username() == null || req.password() == null) {
            throw new DataAccessException("Error: bad request");
        }
        //get userfrom database then verify user + password
        UserData user = userDAO.getUser(req.username());

        if(user == null || !user.password().equals(req.password())){
            throw new DataAccessException("Error: unauthorized");
        }

        String authToken = UUID.randomUUID().toString();

        AuthData auth = new AuthData(authToken, req.username());
        authDAO.createAuth(auth);

        return new LoginResult(req.username(), authToken);
    }

    public void logout(String authToken) throws DataAccessException {
        authDAO.deleteAuth(authToken);
    }

    public void clear() throws DataAccessException{
        userDAO.clear();
        authDAO.clear();

    }

}



