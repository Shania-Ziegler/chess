package service;

import dataaccess.*;
import service.*;
import model.*;
import org.junit.jupiter.api.*;

public class GameServiceTest {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserService userService;
    private GameService gameService;;
}

@BeforeEach
public void setup(){
    userDAO = new MemoryUserDAO();
    authDAO = new MemoryAuthDAO();
    gameDA0 = new MemoryGameDAO();
    userService = new UserService(userDAO, authDAO);
    gameService = new GameService(gameDAO, authDAO);
}
