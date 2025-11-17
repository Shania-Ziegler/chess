package client.test;

import client.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;

public class ServerFacadeTests {
    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }
@AfterAll
    static void stopServer(){
        server.stop();
}
@BeforeEach
    void clearDatabase() throws Exception{
        facade.clearDatabase();
}
    @Test
    void registerPositive() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        Assertions.assertNotNull(authData);
        Assertions.assertTrue(authData.authToken().length() > 10);
    }
    @Test
    void registerNegative() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        Assertions.assertThrows(Exception.class, () ->
                facade.register("player1", "pass2", "p2@email.com"));
    }

    @Test
    void loginPositive() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authData = facade.login("player1", "password");
        Assertions.assertNotNull(authData.authToken());
    }

    @Test
    void loginNegative() throws Exception {
        Assertions.assertThrows(Exception.class, () ->
                facade.login("nonexistent", "password"));
    }

    @Test
    void logoutPositive() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        Assertions.assertDoesNotThrow(() -> facade.logout(authData.authToken()));
    }

    @Test
    void logoutNegative() throws Exception {
        Assertions.assertThrows(Exception.class, () ->
                facade.logout("invalid-token"));
    }

    @Test
    void createGamePositive() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        var result = facade.createGame(authData.authToken(), "TestGame");
        Assertions.assertTrue(result.gameID() > 0);
    }

    @Test
    void createGameNegative() throws Exception {
        Assertions.assertThrows(Exception.class, () ->
                facade.createGame("invalid-token", "TestGame"));
    }

    @Test
    void listGamesPositive() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        facade.createGame(authData.authToken(), "Game1");
        var result = facade.listGames(authData.authToken());
        Assertions.assertNotNull(result.games());
        Assertions.assertTrue(result.games().length >= 1);
    }

    @Test
    void listGamesNegative() throws Exception {
        Assertions.assertThrows(Exception.class, () ->
                facade.listGames("invalid-token"));
    }

    @Test
    void joinGamePositive() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        var gameResult = facade.createGame(authData.authToken(), "TestGame");
        Assertions.assertDoesNotThrow(() ->
                facade.joinGame(authData.authToken(), "WHITE", gameResult.gameID()));
    }

    @Test
    void joinGameNegative() throws Exception {
        var authData1 = facade.register("player1", "password1", "p1@email.com");
        var authData2 = facade.register("player2", "password2", "p2@email.com");
        var gameResult = facade.createGame(authData1.authToken(), "TestGame");
        facade.joinGame(authData1.authToken(), "WHITE", gameResult.gameID());
        Assertions.assertThrows(Exception.class, () ->
                facade.joinGame(authData2.authToken(), "WHITE", gameResult.gameID()));
    }
}


