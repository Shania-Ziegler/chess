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
        javalin.get("/game", this::listGames);
        javalin.post("/game",this::createGame);
        javalin.put("/game", this::joinGame);
        javalin.delete("/db", this::clear);

        javalin.exception(DataAccessException.class, this::handleException);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }


    private void register(Context ctx) throws DataAccessException {
    var request = gson.fromJson(ctx.body(), UserService.RegisterRequest.class);
    var result = userService.register(request); //call service

    //convert to JSON send back to client
    ctx.status(200);
    ctx.json(gson.toJson(result));
}

private void logout(Context ctx) throws DataAccessException{
    String authToken = ctx.header("authorization");
    userService.logout(authToken);
    ctx.status(200);
    ctx.json("{}");
}

private void login(Context ctx) throws DataAccessException{
    var request = gson.fromJson(ctx.body(), UserService.LoginRequest.class);
    var result = userService.login(request);
    ctx.status(200);
    ctx.json(gson.toJson(result));
}
private void listGames(Context ctx) throws DataAccessException {
    String authToken = ctx.header("authorization");
    var result = gameService.listGames(authToken);
    ctx.status(200);
    ctx.json(gson.toJson(result));
}

private void createGame(Context ctx) throws DataAccessException {
    String authToken = ctx.header("authorization");
    var request = gson.fromJson(ctx.body(), GameService.CreateGameRequest.class);
    var result = gameService.createGame(authToken, request);
    ctx.status(200);
    ctx.json(gson.toJson(result));
}

private void joinGame(Context ctx) throws DataAccessException {
    String authToken = ctx.header("authorization");
    var request = gson.fromJson(ctx.body(), GameService.JoinGameRequest.class);
    gameService.joinGame(authToken, request);
    ctx.status(200);
    ctx.json("{}");
}

private void clear(Context ctx) throws DataAccessException {
    userService.clear();
    gameService.clear();
    ctx.status(200);
    ctx.json("{}");
}

private void handleException(DataAccessException ex, Context ctx) {
    String message = ex.getMessage();
    int status = 500;

    if (message.contains("bad request")) {
        status = 400;
    } else if (message.contains("unauthorized")) {
        status = 401;
    } else if (message.contains("already taken")) {
        status = 403;
    }

    ctx.status(status);
    ctx.json(gson.toJson(Map.of("message", message)));
}
}









