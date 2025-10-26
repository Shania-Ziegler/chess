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



}
