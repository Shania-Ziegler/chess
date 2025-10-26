package dataaccess;

import model.UserData;


//Interface that manages the user data by dealing with  saving and finding the users


public interface UserDAO {

    void clear() throws DataAccessException; //Removes all users from storage as test

    void createUser(UserData user) throws DataAccessException; // save new user into storage

    UserData getUser(String username) throws DataAccessException; //find user by username

}
