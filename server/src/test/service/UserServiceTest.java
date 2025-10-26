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

        userService = new (userDAO, authDAO);
    }

    @Test
    @DisplayName("Register Success")

    public void registerSuccess() throws DataAccessException {
        var request = new UserService.RegisterRequest("zap", "password!", "email12")
    }
}