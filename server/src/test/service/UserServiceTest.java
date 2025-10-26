package service;

import dataaccess.*;
import service.*;
import model.*;
import org.junit.jupiter.api.*;

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

        //verify correct results

        assertNotNull(result.authToken());

        assertEquals("zap",result.username());
    }

    @Test
    @DisplayName("Register for duplicate username test")

    public void registerDuplicate() throws DataAccessException{
        var req1 = new UserService.RegisterRequest("zap","password!","email12");userService.register(req1);

        var req2 = new UserService.RegisterRequest("zap","pas123","ziggles");


        //check if code throws excpected exception with () ->
        var exception = assertThrows(DataAccessException.class,() -> userService.register(req2));

        assertTrue(exception.getMessage().contains("Username 'zap' already exists"));
    }




}