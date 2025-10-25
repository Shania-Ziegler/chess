package dataAccess;

import model.UserData;
import java.util.HashMap;
import java.util.Map.


public class MemoryUserDAO implements UserDAO{

    private final Map<String, UserData> users = ?;

    @Override
    public void clear() throws DataAccessException{
        //my hashmap method and empty hash map
    }

    @Override
    public void createUser(UserData user) throws DataAccessException{
        //check if user is null or user name is null throw error for bad request if so


    /*check if user exist in hash map
    since has keeps a key do hashmap contains key username
    if so throw new dataaccess exception with already taken :)



        part 3 3rd method i can think of for now add user to hash map with hashmap key
        then key will become user and value username
     */
    }

    @Override
    public UserData getUser(String username) throws DataAccessException{
        /*
        look up username in hash map return user data return value or null
         */
        return key;
    }

}
