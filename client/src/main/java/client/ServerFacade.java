

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


    public void clearDatabase() throws Exception {
        makeRequest("DELETE", "/db", null, null);
    }

    public AuthData register(String username, String password, String email) throws Exception {
        var path = "/user";
        record RegisterRequest(String username, String password, String email) {}
        var request = new RegisterRequest(username, password, email);
        return makeRequest("POST", path, request, AuthData.class);
    }

    public AuthData login(String username, String password) throws Exception {
        var path = "/session";
        record LoginRequest(String username, String password) {}
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

    // Helper method for requests without auth token
    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        return makeRequest(method, path, request, responseClass, null);
    }

    // Main HTTP request method
    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws Exception {
        try {
            URL url = new URL(serverUrl + path);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            // Add authorization header if provided
            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }

            // Write request body 
            if (request != null) {
                http.addRequestProperty("Content-Type", "application/json");
                String reqData = gson.toJson(request);
                try (OutputStream reqBody = http.getOutputStream()) {
                    reqBody.write(reqData.getBytes());
                }
            }

            http.connect();
            throwIfNotSuccessful(http);

            // Read response body
            if (responseClass != null) {
                return readBody(http, responseClass);
            }
            return null;

        } catch (Exception ex) {
            throw new Exception("Error: " + ex.getMessage());
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
        var status = http.getResponseCode();
        if (status / 100 != 2) {
            throw new Exception("Server returned error: " + status);
        }
    }

    private <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader reader = new InputStreamReader(respBody);
            return gson.fromJson(reader, responseClass);
        }
    }

    // Result record classes
    public record CreateGameResult(int gameID) {}
    public record ListGamesResult(GameData[] games) {}
}
















