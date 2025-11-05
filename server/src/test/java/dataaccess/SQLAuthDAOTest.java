package dataaccess;
//test
import model.AuthData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class SQLAuthDAOTest {
    private static SQLAuthDAO authDAO;

    private static final String TEST_TOKEN = "token123";
    private static final String TEST_USER = "tester";

    @BeforeAll
    public static void init() throws DataAccessException {
        authDAO = new SQLAuthDAO();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO.clear();
    }

    private AuthData createverifytestAuth() throws DataAccessException{
        AuthData auth = new AuthData(TEST_TOKEN, TEST_USER);
        authDAO.createAuth(auth);

        AuthData retrieved = authDAO.getAuth(TEST_TOKEN);
        assertNotNull(retrieved);
        assertEquals(TEST_TOKEN, retrieved.authToken());
        assertEquals(TEST_USER, retrieved.username());

        return retrieved;
    }

    @Test
    @DisplayName("Create Auth - Positive")
    public void createAuthPositive() throws DataAccessException {
        createverifytestAuth();
    }

    @Test
    @DisplayName("Create Auth - Negative (Duplicate Token)")
    public void createAuthNegative() throws DataAccessException {
        AuthData auth = new AuthData("token123", "tester");
        authDAO.createAuth(auth);

        assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(auth);
        });
    }

    @Test
    @DisplayName("Get Auth - Positive")
    public void getAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("token123", "tester");
        authDAO.createAuth(auth);

        AuthData retrieved = authDAO.getAuth("token123");
        assertNotNull(retrieved);
        assertEquals("token123", retrieved.authToken());
        assertEquals("tester", retrieved.username());
    }

    @Test
    @DisplayName("Get Auth - Negative (Not Found)")
    public void getAuthNegative() throws DataAccessException {
        AuthData retrieved = authDAO.getAuth("nonexistent");
        assertNull(retrieved);
    }

    @Test
    @DisplayName("Delete Auth: Positive")
    public void deleteAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("token123", "tester");
        authDAO.createAuth(auth);

        authDAO.deleteAuth("token123");

        AuthData retrieved = authDAO.getAuth("token123");
        assertNull(retrieved);
    }

    @Test
    @DisplayName("Delete Auth: Negative (Not Found)")
    public void deleteAuthNegative() throws DataAccessException {
        assertDoesNotThrow(() -> {
            authDAO.deleteAuth("nonexistent");
        });
    }

    @Test
    @DisplayName("Clear Auth: Positive")
    public void clearPositive() throws DataAccessException {
        AuthData auth1 = new AuthData("token1", "user1");
        AuthData auth2 = new AuthData("token2", "user2");
        authDAO.createAuth(auth1);
        authDAO.createAuth(auth2);

        authDAO.clear();

        assertNull(authDAO.getAuth("token1"));
        assertNull(authDAO.getAuth("token2"));
    }
}