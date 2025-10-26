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

        GameData game = gameDAO.getGame(req.GameID());
        if(game == null){
            throw new DataAccessException("Error bar request");
        }
        //check colors update game
        String color = req.playerColor().toUpperCase();
        if(color.equals("WHITE")) {
            if (game.whiteUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }
            GameData updateGame = new GameData(game.gameID(), auth.username(), game.blackUsername,game.gameName, game.game);
            gameDAO.updateGame(updateGame);
        }else if (color.equals("BLACK")){
            if(game.blackUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }
            GameData updateGame = new GameData(game.gameID(),game.whiteUsername(),auth.username(),game.gameName(),game.game());
            gameDAO.updateGame(updateGame);
            }else {
            throw new DataAccessException("Error: bad request");
        }

        }
        public void clear() throws DataAccessException{
        gameDAO.clear();
        }
        }

}


