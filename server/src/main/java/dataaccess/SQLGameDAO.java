package dataaccess;

import model.GameData;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    @Override
    public int createGame(String gameName) throws DataAccessException {
        throw new DataAccessException("Not implemented yet");
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        throw new DataAccessException("Not implemented yet");
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        throw new DataAccessException("Not implemented yet");
    }

    @Override
    public void updateGame(int gameID, GameData game) throws DataAccessException {
        throw new DataAccessException("Not implemented yet");
    }

    @Override
    public void clear() throws DataAccessException {
        throw new DataAccessException("Not implemented yet");
    }
}git