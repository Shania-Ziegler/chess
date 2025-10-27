package service;

import dataaccess.*;
import service.*;
import model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserService userService;
    private GameService gameService;


@BeforeEach
public void setup(){
    userDAO = new MemoryUserDAO();
    authDAO = new MemoryAuthDAO();
    gameDAO = new MemoryGameDAO();
    userService = new UserService(userDAO, authDAO);
    gameService = new GameService(gameDAO, authDAO);
}

    private String registerGetToken(String username) {
        var request = new UserService.RegisterRequest(username, "password", username + "@test.com");
            var result = assertDoesNotThrow(() -> userService.register(request));
        return result.authToken();
    }

    @Test
    @DisplayName("Create Game Success")
    public void createGameSuccess() throws DataAccessException {
    String token = registerGetToken("zaps!");

    var gameReq = new GameService.CreateGameRequest("Chessmatch");
    var result = gameService.createGame(token, gameReq);

    assertNotNull(result.gameID(), "Game ID should not be null.");
    assertTrue(result.gameID() > 0, "Game ID should be a positive integer");
}
    @Test
    @DisplayName("Create Game Unauthorized")
    public void createGameUnauthorized() {

        var gameReq = new GameService.CreateGameRequest("My Game");
        var exception = assertThrows(DataAccessException.class,() -> gameService.createGame("bad-tokens", gameReq));

    assertTrue(exception.getMessage().contains("unauthorized"),"Exception must contain 'unauthorized' for a bad token.");
}
    @Test
    @DisplayName("List Games Success")
    public void listGamesSuccess() {
    // Arrange
    String token = registerGetToken("player2");
    var gameReq = new GameService.CreateGameRequest("Game One");
    assertDoesNotThrow(() -> gameService.createGame(token, gameReq));


    var result = assertDoesNotThrow(() -> gameService.listGames(token));


    assertNotNull(result.games(), "The games list should not be null.");
    assertEquals(1, result.games().size(), "games list must have exactly one game.");
}

    @Test
    @DisplayName("List Games Unauthorized")
    public void listGamesUnauthorized() {
        var exception = assertThrows(DataAccessException.class,
                () -> gameService.listGames("invalid-auth-token"));
        assertTrue(exception.getMessage().contains("unauthorized"));
    }

    @Test
    @DisplayName("Join Game Success")
    public void joinGameSuccess() {
    String token = registerGetToken("player3");
    var gameReq = new GameService.CreateGameRequest("Tournament Game");
    var createResult = assertDoesNotThrow(() -> gameService.createGame(token, gameReq));
    var joinReq = new GameService.JoinGameRequest("WHITE", createResult.gameID());


    assertDoesNotThrow(() -> gameService.joinGame(token, joinReq));
}

    @Test
    @DisplayName("Join Game Already Taken")
    public void joinGameAlreadyTaken() {
    // Arrange
    String token1 = registerGetToken("player4");
    String token2 = registerGetToken("player5");
    var gameReq = new GameService.CreateGameRequest("Competitive Match");
    var createResult = assertDoesNotThrow(() -> gameService.createGame(token1, gameReq));

    // First player joins WHITE
    var joinReq1 = new GameService.JoinGameRequest("WHITE", createResult.gameID());
    assertDoesNotThrow(() -> gameService.joinGame(token1, joinReq1));

    // Second player tries to join the same color
    var joinReq2 = new GameService.JoinGameRequest("WHITE", createResult.gameID());


    var exception = assertThrows(DataAccessException.class, () -> gameService.joinGame(token2, joinReq2));

    assertTrue(exception.getMessage().contains("already taken"));
}
    @Test
    @DisplayName("Clear Remove All Games")
    public void clearGames() throws DataAccessException {

    String token = registerGetToken("player6");
    var gameReq = new GameService.CreateGameRequest("Test Game"); assertDoesNotThrow(() -> gameService.createGame(token, gameReq));assertDoesNotThrow(() -> gameService.clear());

    var result = assertDoesNotThrow(() -> gameService.listGames(token));assertEquals(0, result.games().size(), "Games list size must be zero after clear.");
}
}
