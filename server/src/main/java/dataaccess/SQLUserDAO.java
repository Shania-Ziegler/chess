package dataaccess;

import model.UserData;

public class SQLUserDAO implements UserDAO {
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
