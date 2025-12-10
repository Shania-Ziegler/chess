package ui;

import chess.*;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import websocket.messages.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

public class GameplayUI implements NotificationHandler {
    private final WebSocketFacade ws;
    private ChessGame currentGame;
    private final String authToken;
    private final int gameID;
    private final ChessGame.TeamColor playerColor; // null if observer
    private final Scanner scanner;
    private final String serverUrl;

    public GameplayUI(String serverUrl, String authToken, int gameID, ChessGame.TeamColor playerColor) throws Exception {
        this.serverUrl = serverUrl;
        this.authToken = authToken;
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.scanner = new Scanner(System.in);

        // Create WebSocket connection
        String wsUrl = serverUrl + "/ws";
        this.ws = new WebSocketFacade(wsUrl, this);


        ws.connect(authToken, gameID);
    }
}