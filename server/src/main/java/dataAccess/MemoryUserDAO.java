package dataAccess;

import model.UserData;
import java.util.HashMap;
import java.util.Map;


public class MemoryUserDAO implements UserDAO{

    private final Map<String, UserData> users = new HashMap<>();

    /*
   do users come from where my user data right i am not sure yet how to implement
   look at the map to see whrre i can put in but need something like

   user.get
   user.put
   ect
     */


    @Override
    public void clear() throws DataAccessException{
        HashMap.clear<>(UserData);

        //my hashmap method and empty hash map
    }

    @Override
    public void createUser(UserData user) throws DataAccessException{
        //check if user is null or user name is null throw error for bad request if so

        if(user == null){
            throw new DataAccessException("Error Bad Request user object is null");
        }

        if(user.username() == null){
            throw new DataAccessException("Username is missing or blank retry");
        }

        //stores user
        user.put(key, value)



    //check if user exist in hash map

    if (users.containsKey(User)){
        throw new DataAccessException("Error username already taken");
    }

    if(users.containsKey  )

    @Override
    public UserData getUser(String username)
        throws DataAccessException{
        /*
        look up username in hash map return user data return value or null
         */
        return key;
    }

}
