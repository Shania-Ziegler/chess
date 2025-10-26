package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.JoinGameRequest;

public class GameService {

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

public GameService(GameDAO gameDAO, AuthDAO authDAO){
    this.gameDAO;
    this.authDAO;
}
    //create game list game

    public record JoinGameRequest(String playerColor, Integer gameID){}

    public record JoinGameResult{}

}

