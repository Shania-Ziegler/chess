package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTest {
    private static SQLUserDAO userDAO;

    @BeforeAll
    public static void init() throws DataAccessException {
        userDAO = new SQLUserDAO();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO.clear();
    }

    @Test
    @DisplayName("Create User : Positive")
    public void createUserPositive() throws DataAccessException {
        UserData user = new UserData("user", "password123", "zaps@email.com");
        userDAO.createUser(user);

        UserData retrieved = userDAO.getUser("user");
        assertNotNull(retrieved);
        assertEquals("user", retrieved.username());
        assertEquals("test@email.com", retrieved.email());
    }

    @Test
    @DisplayName("Create User : Negative (Duplicate)")
    public void createUserNegative() throws DataAccessException {
        UserData user = new UserData("user", "password123", "zaps@email.com");
        userDAO.createUser(user);

        assertThrows(DataAccessException.class, () -> {userDAO.createUser(user);});
    }

    @Test
    @DisplayName("Get User - Positive")
    public void getUserPositive() throws DataAccessException {
        UserData user = new UserData("user", "password123", "zaps@email.com");
        userDAO.createUser(user);

        UserData retrieved = userDAO.getUser("user");
        assertNotNull(retrieved);
        assertEquals("user", retrieved.username());
        assertEquals("zaps@email.com", retrieved.email());
    }

    @Test
    @DisplayName("Get User : Negative (Not Found)")
    public void getUserNegative() throws DataAccessException {
        UserData retrieved = userDAO.getUser("nonexistent");
        assertNull(retrieved);
    }

    @Test
    @DisplayName("Clear Users - Positive")
    public void clearPositive() throws DataAccessException {
        UserData user1 = new UserData("user1", "pass1", "zapple@test.com");
        UserData user2 = new UserData("user2", "pass2", "bob@test.com");
        userDAO.createUser(user1);
        userDAO.createUser(user2);

        userDAO.clear();

        assertNull(userDAO.getUser("user1"));
        assertNull(userDAO.getUser("user2"));
    }
}