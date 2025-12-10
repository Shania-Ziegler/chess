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
        System.out.println("Welcome Please Type 'help' for the relevant commands.");

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
                    case "move" -> handleMoveCommand(parts);
                    case "resign" -> resign();
                    case "highlight" -> handleHighlightCommand(parts);
                    case "quit" -> {
                        leave();
                        running = false;
                    }
                    default -> System.out.println("Unknown command. Type 'help' for available commands.");
                }
            } catch (Exception e) {
                System.out.println("Something has gone wrong: " + e.getMessage());
            }
        }
    }

    private void handleMoveCommand(String[] parts) throws IOException {
        if (parts.length < 3) {
            System.out.println("To make move type: move e2 e4");
            System.out.println("This makes the piece move to the relevant position");
        } else {
            makeMove(parts[1], parts[2]);
        }
    }

    private void handleHighlightCommand(String[] parts) {
        if (parts.length < 2) {
            System.out.println("To see the allowed moves type: highlight e2 or other position");
        } else {
            highlightMoves(parts[1]);
        }
    }

    private void displayMenu(){
        System.out.println("Chess game Help Commands:");
        System.out.println("  help - Display this help message next time you're stuck");
        System.out.println("  redraw - Redraw the chess board");
        System.out.println("  leave - Leave the game");
        System.out.println("  Make move start + end (move e2 e4 ect)");
        System.out.println("  resign - Forfeit the game");
        System.out.println(" Show position legal moves for a piece ( example: show e2)");
    }
    private void drawBoard() {
        if (currentGame == null) {
            System.out.println("Loading Game");
            return;
        }
        ChessGame.TeamColor perspective = (playerColor != null) ? playerColor : ChessGame.TeamColor.WHITE;
        BoardDrawer.drawBoard(currentGame, perspective);
    }
    private void leave() throws IOException {
        ws.leave(authToken, gameID);
        ws.close();
        System.out.println("You Have left the game.");
    }
    private void makeMove(String startPos, String endPos) throws IOException {
        if (playerColor == null) {
            System.out.println("Hy you're watching observers cannot make moves type help to join game.");
            return;
        }

        if (currentGame == null) {
            System.out.println("Game not loaded yet.");
            return;
        }

        try {
            ChessPosition start = parsePosition(startPos);
            ChessPosition end = parsePosition(endPos);

            ChessPiece.PieceType promotion = checkForPawnPromotion(start, end);
            ChessMove move = new ChessMove(start, end, promotion);
            ws.makeMove(authToken, gameID, move);

        } catch (Exception e) {
            System.out.println("Invalid move format. Please enter something like: move e2 e4");
        }
    }

    private ChessPiece.PieceType checkForPawnPromotion(ChessPosition start, ChessPosition end) {
        ChessPiece piece = currentGame.getBoard().getPiece(start);

        if (piece == null || piece.getPieceType() != ChessPiece.PieceType.PAWN) {
            return null;
        }

        boolean whitePromotion = piece.getTeamColor() == ChessGame.TeamColor.WHITE && end.getRow() == 8;
        boolean blackPromotion = piece.getTeamColor() == ChessGame.TeamColor.BLACK && end.getRow() == 1;

        if (whitePromotion || blackPromotion) {
            return promptForPromotion();
        }

        return null;
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
            System.out.println("Still loading game please wait.");
            return;
        }

        try {
            ChessPosition position = parsePosition(posStr);
            Collection<ChessMove> validMoves = currentGame.validMoves(position);

            if (validMoves == null || validMoves.isEmpty()) {
                System.out.println("That piece can't move please try another.");
                return;
            }

            System.out.println("\nLegal moves for piece at " + posStr + ":");
            for (ChessMove move : validMoves) {
                ChessPosition end = move.getEndPosition();
                char col = (char)('a' + end.getColumn() - 1);
                System.out.println("  → " + col + end.getRow());
            }
            System.out.println();

        } catch (Exception e) {
            System.out.println("That position doesn't exist. Retry with command like: highlight e2");
        }
    }

    private ChessPosition parsePosition(String pos) {
        if (pos.length() != 2) {
            throw new IllegalArgumentException("Invalid position given type help if you need further assistance");
        }

        char colChar = Character.toLowerCase(pos.charAt(0));
        char rowChar = pos.charAt(1);

        int col = colChar - 'a' + 1; // a=1, b=2, ..., h=8
        int row = Character.getNumericValue(rowChar);

        if (col < 1 || col > 8 || row < 1 || row > 8) {
            throw new IllegalArgumentException("Position out of bounds must be a-h and 1-8");
        }

        return new ChessPosition(row, col);
    }

    private ChessPiece.PieceType promptForPromotion() {
        System.out.println("\n Your pawn has reached the end! Please choose what to promote it to:");
        System.out.println("  Q - Queen");
        System.out.println("  R - Rook");
        System.out.println("  B - Bishop");
        System.out.println("  N - Knight");
        System.out.print("Type your choice (Q/R/B/N): ");

        String choice = scanner.nextLine().trim().toUpperCase();

        return switch (choice) {
            case "Q" -> ChessPiece.PieceType.QUEEN;
            case "R" -> ChessPiece.PieceType.ROOK;
            case "B" -> ChessPiece.PieceType.BISHOP;
            case "N" -> ChessPiece.PieceType.KNIGHT;
            default -> {
                System.out.println("No choice? Will make this into Queen");
                yield ChessPiece.PieceType.QUEEN;
            }
        };
    }
}
