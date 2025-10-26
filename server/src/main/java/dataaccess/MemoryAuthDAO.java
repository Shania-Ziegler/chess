package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    private final Map<String, AuthData> authTokens = new HashMap<>();

    @Override
    public void clear() throws DataAccessException{
        authTokens.clear();
    }


    //check if we have valid token + put auth data in the hash map using token as key value
    @Override
    public void createAuth(AuthData auth) throws DataAccessException{
        if(auth == null || auth.authToken() == null){
            throw new DataAccessException("Error: bad request");
        }
        authTokens.put(auth.authToken(), auth);
    }



    @Override
    public AuthData getAuth(String authToken) throws DataAccessException{
        return authTokens.get(authToken);
    }



    @Override
    public void deleteAuth(String authToken) throws DataAccessException{
        if(authToken == null || !authTokens.containsKey(authToken)){
            throw new DataAccessException("Error: unauthorized");
        }
     authTokens.remove(authToken);
    }

}
