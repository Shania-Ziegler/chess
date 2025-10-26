package dataaccess;

import model.GameData;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    //look up game via id number
    private final Map<Integer, GameData> games = new HashMap<>();


    //adding the increment keeping track of game
    private int nextGameID = 1;


    @Override
    private int CreateGame(GameData game) throws DataAccessException{
        if (game == null || game.gameName() == null){
            throw new DataAccessException("Error: bad request");
        }

        int gameID = nextGameID;

        //create the game data with the real id
    }
    return gameID;

}
