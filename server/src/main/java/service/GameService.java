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

    public record CreateGameRequest{String gameName){}
    public record JoinGameRequest(String playerColor, Integer gameID){}
    public record ListGameResult(Collection<GameData> games){}}
    public record CreateGameResult(String gameName){}

    public CreateGameResult createGame(String authToken, CreateGameRequest req) throws DataAccessException{
    validateAuth(authToken);

    if(req.gameName()==null){
        throw new DataAccessException("Error: bad request");
    }

    GameData game = new GameData(0,null,null,req.gameName(), new ChessGame());
    int gameID = gameDAO.createGame(game);

    return new CreateGameResult(gameID);

    public ListGamesResult listGames(String authToken) throws DataAccessException{
        validateAuth(authToken);

        Collection<GameData> games = GameDAO.listGames();
        return new ListGamesResult(game);
        }

        public void joinGame(String authToken, JoinGameRequest req)throws DataAccessException{
        AuthData auth = validateAuth(authToken);

        if(req.gameID() == null || req.playerColor()==null){
            throw new DataAccessException("Error bad request")
        }


        }

}


