package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import chess.ChessGame;
import java.util.Collection;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

public GameService(GameDAO gameDAO, AuthDAO authDAO){
    this.gameDAO = gameDAO;
    this.authDAO = authDAO;
}
    //create game list game

    public record CreateGameRequest{String gameName){}
    public record JoinGameRequest(String playerColor, Integer gameID){}
    public record ListGameResult(Collection<GameData> games){}}
    public record CreateGameResult(String gameName){}

    public CreateGameResult createGame(String authToken, CreateGameRequest req) throws DataAccessException{
    validateAuth(authToken);

    if(req.gameName()==null){
        throw new DataAccessException("Error: bad request");
    }

    GameData game = new GameData(0)

}

