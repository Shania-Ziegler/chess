package ui;

import chess.*;
import java.util.Collection;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import websocket.messages.*;

import java.io.IOException;
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

    private void handleLoadGame(LoadGameMessage msg) {
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
                    case "help" -> displayMenu();
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

    private void displayMenu(){
        System.out.println("Available commands:");
        System.out.println("  help - Display this help message next time you're stuck");
        System.out.println("  redraw - Redraw the chess board");
        System.out.println("  leave - Leave the game");
        System.out.println("  Make move start + end (move e2 e4 ect)");
        System.out.println("  resign - Forfeit the game");
        System.out.println(" Show position legal moves for a piece ( example: show e2)");
    }
    private void drawBoard() {
        if (currentGame == null) {
            System.out.println("Waiting for game data");
            return;
        }
        boolean whiteOnBottom = (playerColor == null || playerColor == ChessGame.TeamColor.WHITE);
        BoardDrawer.drawBoard(currentGame.getBoard(), whiteOnBottom, null);
    }
    private void leave() throws IOException {
        ws.leave(authToken, gameID);
        ws.close();
        System.out.println("Left the game.");
    }
    private void makeMove(String startPos, String endPos) throws IOException {
        if (playerColor == null) {
            System.out.println("Observers cannot make moves.");
            return;
        }

        if (currentGame == null) {
            System.out.println("Game not loaded yet.");
            return;
        }

        try {
            ChessPosition start = parsePosition(startPos);
            ChessPosition end = parsePosition(endPos);

            ChessPiece piece = currentGame.getBoard().getPiece(start);
            ChessPiece.PieceType promotion = null;

            if (piece != null && piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                if ((piece.getTeamColor() == ChessGame.TeamColor.WHITE && end.getRow() == 8) ||
                        (piece.getTeamColor() == ChessGame.TeamColor.BLACK && end.getRow() == 1)) {
                    promotion = promptForPromotion();
                }
            }
            ChessMove move = new ChessMove(start, end, promotion);
            ws.makeMove(authToken, gameID, move);

        } catch (Exception e) {
            System.out.println("Invalid move format. Use format like: move e2 e4");
        }
    }

    private void resign() throws IOException {
        if (playerColor == null) {
            System.out.println("Observers cannot resign.");
            return;
        }

        System.out.print("Are you sure you want to resign? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("yes") || confirmation.equals("y")) {
            ws.resign(authToken, gameID);
            System.out.println("You have resigned from the game.");
        } else {
            System.out.println("Resign cancelled.");
        }
    }

    private void highlightMoves(String posStr) {
        if (currentGame == null) {
            System.out.println("Game not loaded yet.");
            return;
        }

        try {
            ChessPosition position = parsePosition(posStr);
            Collection<ChessMove> validMoves = currentGame.validMoves(position);

            if (validMoves == null || validMoves.isEmpty()) {
                System.out.println("No valid moves for that piece.");
                return;
            }

            // Draw board with highlights
            boolean whiteOnBottom = (playerColor == null || playerColor == ChessGame.TeamColor.WHITE);
            BoardDrawer.drawBoard(currentGame.getBoard(), whiteOnBottom, validMoves);

        } catch (Exception e) {
            System.out.println("Invalid position format. Use format like: highlight e2");
        }
    }

    private ChessPosition parsePosition(String pos) {
        if (pos.length() != 2) {
            throw new IllegalArgumentException("Invalid position given");
        }

        char colChar = Character.toLowerCase(pos.charAt(0));
        char rowChar = pos.charAt(1);

        int col = colChar - 'a' + 1; // a=1, b=2, ..., h=8
        int row = Character.getNumericValue(rowChar);

        if (col < 1 || col > 8 || row < 1 || row > 8) {
            throw new IllegalArgumentException("Position out of bounds");
        }

        return new ChessPosition(row, col);
    }

    private ChessPiece.PieceType promptForPromotion() {
        System.out.println("Promote pawn to: (Q)ueen, (R)ook, (B)ishop, (K)night by using upper case first letter of piece");
        System.out.print(">>> ");
        String choice = scanner.nextLine().trim().toLowerCase();

        return switch (choice) {
            case "Q", "queen" -> ChessPiece.PieceType.QUEEN;
            case "R", "rook" -> ChessPiece.PieceType.ROOK;
            case "B", "bishop" -> ChessPiece.PieceType.BISHOP;
            case "K", "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> ChessPiece.PieceType.QUEEN; // Default to queen if they dont pick
        };
    }
}
