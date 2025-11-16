package client;

import com.google.gson.Gson;
import model.*;
import java.io.*;
import java.net.*;


public class ServerFacade {

    private final String serverUrl;
    private final Gson gson = new Gson();

    public ServerFacade(String url) {
        this.serverUrl = url;
    }

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }


    public AuthData register(String username, String password, String email) throws Exception {
        var path = "/user";
        record RegisterRequest(String username, String password, String email) {
        }
        var request = new RegisterRequest(username, password, email);
        return makeRequest("POST", path, request, AuthData.class);
    }

    public AuthData login(String username, String password) throws Exception {
        var path = "/session";
        record LoginRequest(String username, String password) {
        }
        var request = new LoginRequest(username, password);
        return makeRequest("POST", path, request, AuthData.class);
    }
    public void logout(String authToken) throws Exception {
        var path = "/session";
        makeRequest("DELETE", path, null, null, authToken);
    }
    public CreateGameResult createGame(String authToken, String gameName) throws Exception {
        var path = "/game";
        record CreateGameRequest(String gameName) {}
        var request = new CreateGameRequest(gameName);
        return makeRequest("POST", path, request, CreateGameResult.class, authToken);
    }
    public ListGamesResult listGames(String authToken) throws Exception {
        var path = "/game";
        return makeRequest("GET", path, null, ListGamesResult.class, authToken);
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws Exception {
        var path = "/game";
        record JoinGameRequest(String playerColor, int gameID) {}
        var request = new JoinGameRequest(playerColor, gameID);
        makeRequest("PUT", path, request, null, authToken);
    }
    private<T> T makeRequest(String method,String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = new URL(serverUrl + path);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
        }

        fjrtoi
    }


}
