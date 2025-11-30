package dataaccess;

import java.chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTest {
    private static SQLGameDAO gameDAO;

    @BeforeAll
    public static void init() throws DataAccessException {
        gameDAO = new SQLGameDAO();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO.clear();
    }

    @Test
    @DisplayName("Create Game - Positive")
    public void createGamePositive() throws DataAccessException {
        GameData game = new GameData(0, null, null, "TestGame", new ChessGame());
        int gameID = gameDAO.createGame(game);

        assertTrue(gameID > 0);
        GameData retrieved = gameDAO.getGame(gameID);
        assertNotNull(retrieved);
        assertEquals("TestGame", retrieved.gameName());
    }

    @Test
    @DisplayName("Create Game - Negative (Null Name)")
    public void createGameNegative() {
        GameData game = new GameData(0, null, null, null, new ChessGame());
        assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(game);
        });
    }

    @Test
    @DisplayName("Get Game - Positive")
    public void getGamePositive() throws DataAccessException {
        GameData game = new GameData(0, null, null, "TestGame", new ChessGame());
        int gameID = gameDAO.createGame(game);

        GameData retrieved = gameDAO.getGame(gameID);
        assertNotNull(retrieved);
        assertEquals(gameID, retrieved.gameID());
        assertEquals("TestGame", retrieved.gameName());
        assertNotNull(retrieved.game());
    }

    @Test
    @DisplayName("Get Game - Negative (Not Found)")
    public void getGameNegative() throws DataAccessException {
        GameData retrieved = gameDAO.getGame(99999);
        assertNull(retrieved);
    }

    @Test
    @DisplayName("List Games - Positive")
    public void listGamesPositive() throws DataAccessException {
        gameDAO.createGame(new GameData(0, null, null, "Game1", new ChessGame()));
        gameDAO.createGame(new GameData(0, null, null, "Game2", new ChessGame()));
        gameDAO.createGame(new GameData(0, null, null, "Game3", new ChessGame()));

        Collection<GameData> games = gameDAO.listGames();
        assertNotNull(games);
        assertEquals(3, games.size());
    }

    @Test
    @DisplayName("List Games - Negative (Empty)")
    public void listGamesNegative() throws DataAccessException {
        Collection<GameData> games = gameDAO.listGames();
        assertNotNull(games);
        assertTrue(games.isEmpty());
    }

    @Test
    @DisplayName("Update Game - Positive")
    public void updateGamePositive() throws DataAccessException {
        GameData game = new GameData(0, null, null, "TestGame", new ChessGame());
        int gameID = gameDAO.createGame(game);
        GameData original = gameDAO.getGame(gameID);

        GameData updated = new GameData(gameID, "whitePlayer", "blackPlayer", "TestGame", original.game());
        gameDAO.updateGame(updated);

        GameData retrieved = gameDAO.getGame(gameID);
        assertEquals("whitePlayer", retrieved.whiteUsername());
        assertEquals("blackPlayer", retrieved.blackUsername());
    }

    @Test
    @DisplayName("Update Game - Negative (Invalid ID)")
    public void updateGameNegative() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(99999, null, null, "FakeGame", game);

        assertDoesNotThrow(() -> {
            gameDAO.updateGame(gameData);
        });
    }

    @Test
    @DisplayName("Clear Games - Positive")
    public void clearPositive() throws DataAccessException {
        gameDAO.createGame(new GameData(0, null, null, "Game1", new ChessGame()));
        gameDAO.createGame(new GameData(0, null, null, "Game2", new ChessGame()));

        gameDAO.clear();

        Collection<GameData> games = gameDAO.listGames();
        assertTrue(games.isEmpty());
    }
}