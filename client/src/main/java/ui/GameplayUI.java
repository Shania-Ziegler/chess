package ui;

import chess.*;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import websocket.messages.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

import static ui.BoardDrawer.drawBoard;

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

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case LOAD_GAME -> handleLoadGame((LoadGameMessage) message);
            case ERROR -> handleError((ErrorMessage) message);
            case NOTIFICATION -> handleNotification((NotificationMessage) message);
        }
    }
    private void handleLoadGame(LoadGameMessage msg){
        this.currentGame = msg.getGame();
        System.out.println();
        drawBoard();
        System.out.print("\n>>>");
    }
    private void handleError(ErrorMessage msg) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "\n" + msg.getErrorMessage() + EscapeSequences.RESET_TEXT_COLOR);
        System.out.print(">>> ");
    }

    private void handleNotification(NotificationMessage msg) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "\n" + msg.getMessage() + EscapeSequences.RESET_TEXT_COLOR);
        System.out.print(">>> ");
    }
    public void run() {
        System.out.println("Entering gameplay. Type 'help' for commands.");

        boolean running = true;
        while (running) {
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            String[] parts = input.split("\\s+");
            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                    case "help" -> displayHelp();
                    case "redraw" -> drawBoard();
                    case "leave" -> {
                        leave();
                        running = false;
                    }
                    case "move" -> {
                        if (parts.length < 3) {
                            System.out.println("Usage: move <start> <end> (e.g., move e2 e4)");
                        } else {
                            makeMove(parts[1], parts[2]);
                        }
                    }
                    case "resign" -> resign();
                    case "highlight" -> {
                        if (parts.length < 2) {
                            System.out.println("Usage: highlight <position> (e.g., highlight e2)");
                        } else {
                            highlightMoves(parts[1]);
                        }
                    }
                    case "quit" -> {
                        leave();
                        running = false;
                    }
                    default -> System.out.println("Unknown command. Type 'help' for available commands.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

}