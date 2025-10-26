package dataaccess;

import model.GameData;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    //look up game with id number
    private final Map<Integer, GameData> games = new HashMap<>();

    //this keeps track of the games
    private int nextGameID = 1;


    @Override
    public void clear() throws DataAccessException{
        games.clear();
        nextGameID = 1;
    }


    @Override
    public int createGame(GameData game) throws DataAccessException {
        if (game == null || game.gameName() == null) {
            throw new DataAccessException("Error: bad request");
        }

        int gameID = nextGameID;
        nextGameID++;

        //create game id with real id
        GameData newGame = new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());

        games.put(gameID,newGame);

        return gameID;

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException{
        return games.values();
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException{
        if(game == null || !games.containsKey(game.gameID())) {
            throw new DataAccessException("Error: bad request");

        }
        games.put(game.gameID(), game);
    }

}
