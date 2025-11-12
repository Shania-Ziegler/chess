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
}