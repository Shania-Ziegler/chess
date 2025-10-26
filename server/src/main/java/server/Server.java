package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.*;
import dataaccess.*; //wild card import
import service.*;
import java.util.Map;


public class Server {

    private final Javalin javalin;
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();

    private final UserService userService = new UserService(userDAO, authDAO);
    private final GameService gameService = new GameService(gameDAO, authDAO);

    private final Gson gson = new Gson();


    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        //register end points
        javalin.post("/user",this::register);
        javalin.post("/session", this::login);
        javalin.delete("/session", this::logout);
        javalin.get("/game: ", this::listGames);
        javalin.post("/game: ",this::createGames);
        javalin.put("/game: ", this::joinGame);
        javalin.delete("/db: ", this::clear);

        javalin.exception(DataAccessException.class, this::handleException);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}


