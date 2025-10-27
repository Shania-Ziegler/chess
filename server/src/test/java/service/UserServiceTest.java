package java.service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*; //use methods without using Assertions. before


public class UserServiceTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;


    //run method before each @Test
    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();

        userService = new UserService(userDAO, authDAO);
    }

    @Test
    @DisplayName("Register Success")
    public void registerSuccess() throws DataAccessException {
        var request = new UserService.RegisterRequest("zap", "password!", "email12");

        var result = userService.register(request);

        //verify correct result

        assertNotNull(result.authToken(),"Auth token needs to be generated");

        assertEquals("zap",result.username(),"Username in result match request");
    }

    @Test
    @DisplayName("Register for duplicate username test")
    public void registerDuplicate() throws DataAccessException{
        var req1 = new UserService.RegisterRequest("zap","password!","email12@mail.com");
        userService.register(req1);

        var req2 = new UserService.RegisterRequest("zap","pas123","ziggles@mail.com");


        //check if code throws expected exception with () ->
        var exception = assertThrows(DataAccessException.class,() -> userService.register(req2));

        assertFalse(exception.getMessage().contains("Username already exists"));
    }

    @Test
    @DisplayName("Login Success")
    public void loginSuccess() throws DataAccessException{
        var registerReq = new UserService.RegisterRequest("emily","pass45","iceberg@mail.com");
        userService.register(registerReq);

        var loginReq = new UserService.LoginRequest("emily","pass45");
        var result = userService.login(loginReq);


        assertNotNull(result.authToken(),"Auth token must be gen on successful login.");
        assertEquals("emily",result.username(), "Logged in username needs to be correct");

    }

    @Test
    @DisplayName("Login Wrong Password")
    public void loginWrongPassword() {
        //  Setup user
        var registerReq = new UserService.RegisterRequest("emily", "mypassword", "emily@test.com");
        assertDoesNotThrow(() -> userService.register(registerReq));

        // Attempt login with wrong password and verify the exception
        var loginReq = new UserService.LoginRequest("emily", "wrongpassword");

        var exception = assertThrows(DataAccessException.class, () -> userService.login(loginReq));

        assertTrue(exception.getMessage().contains("unauthorized"), "Exception message must contain 'unauthorized' for wrong credientials.");
    }

    @Test
    @DisplayName("Logout Success")
    public void logoutSuccess() {
        //Register and get a valid auth token
        var registerReq = new UserService.RegisterRequest("zapsy", "pass456", "zapsy@test.com");
        var registerResult = assertDoesNotThrow(() -> userService.register(registerReq));

        //  Logout should execute without throwing an exception
        assertDoesNotThrow(() -> userService.logout(registerResult.authToken()),"Logout with a valid token should not throw an exception.");
    }
    @Test
    @DisplayName("Logout Invalid Token")
    public void logoutInvalidToken() {
        // Arrange
        String invalidToken = "invalid-token-12";

        // Act & Assert: Attempt logout with invalid token and verify the exception
        var exception = assertThrows(DataAccessException.class, () -> userService.logout(invalidToken));

        assertTrue(exception.getMessage().contains("unauthorized"), "Exception message must contain 'unauthorized' for an invalid token.");
    }

    @Test
    @DisplayName("Clear Removes All Users")
    public void clearSuccess() throws DataAccessException {

        var registerReq = new UserService.RegisterRequest("zapsy", "password", "zapsy@email.com");
        userService.register(registerReq);


        userService.clear();

        var loginReq = new UserService.LoginRequest("zapsy", "password");

        var exception = assertThrows(DataAccessException.class, () -> userService.login(loginReq));

        assertTrue(exception.getMessage().contains("unauthorized"), "After clear, login should fail");
    }
}

