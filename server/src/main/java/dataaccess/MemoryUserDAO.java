package dataaccess;

import model.UserData;
import java.util.HashMap;
import java.util.Map;


public class MemoryUserDAO implements UserDAO {

    private final Map<String, UserData> users = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        users.clear(); //remove user from hashmap
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if (user == null || user.username() == null) {
            throw new DataAccessException("Error: bad Request");
        }


        if(users.containsKey(user.username())){
            throw new DataAccessException("Error: username already taken");
        }
    users.put(user.username(), user); //stores user
    }



    //check if user exist in hash map
    @Override
    public UserData getUser(String username)
        throws DataAccessException {
        /*
        look up username in hash map return user data return value or null
         */
        return users.get(username);
    }

}
