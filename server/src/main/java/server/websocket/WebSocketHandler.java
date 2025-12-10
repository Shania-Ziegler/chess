package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;
import model.AuthData;
import model.GameData;
import websocket.commands.*;
import websocket.messages.*;

public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final Gson gson = new Gson();
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void onConnect(WsConnectContext ctx) {
        System.out.println("WebSocket connected: ");
    }
    public void onClose(io.javalin.websocket.WsCloseContext ctx) {
        connections.removeSession(ctx);
        System.out.println("Connection removed from all games");
    }

    public void onMessage(WsMessageContext ctx) {
        try {
            String message = ctx.message();
            System.out.println("Received: " + message);

            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

            switch (command.getCommandType()) {
                case CONNECT -> handleConnect(ctx, command, message);
                case MAKE_MOVE -> handleMakeMove(ctx, command, message);  // Pass message
                case LEAVE -> handleLeave(ctx, command);
                case RESIGN -> handleResign(ctx, command);
            }
        } catch (Exception e) {
            sendError(ctx, "Error: " + e.getMessage());
        }
    }

    private void handleConnect(WsMessageContext ctx, UserGameCommand command, String message) {
        try {
            // Validate authToken
            AuthData auth = authDAO.getAuth(command.getAuthToken());
            if (auth == null) {
                sendError(ctx, "Error: Invalid auth token");
                return;
            }

            // Validate gameID
            GameData game = gameDAO.getGame(command.getGameID());
            if (game == null) {
                sendError(ctx, "Error: Game not found");
                return;
            }

            // Add to connection manager
            connections.add(command.getGameID(), command.getAuthToken(), ctx);

            // Send LOAD_GAME to root client
            LoadGameMessage loadMsg = new LoadGameMessage(game.game());
            connections.sendToSession(ctx, loadMsg);

            // Determine role and send notification
            String username = auth.username();
            String notificationMsg;

            if (username.equals(game.whiteUsername())) {
                notificationMsg = username + " joined the game as WHITE";
            } else if (username.equals(game.blackUsername())) {
                notificationMsg = username + " joined the game as BLACK";
            } else {
                notificationMsg = username + " joined the game as an observer";
            }

            NotificationMessage notifyMsg = new NotificationMessage(notificationMsg);
            connections.broadcast(command.getGameID(), notifyMsg, ctx);

        } catch (Exception e) {
            sendError(ctx, "Error: " + e.getMessage());
        }
    }

    private void handleLeave(WsMessageContext ctx, UserGameCommand command) {
        try {
            // Validate auth
            AuthData auth = authDAO.getAuth(command.getAuthToken());
            if (auth == null) {
                sendError(ctx, "Error: Invalid auth token");
                return;
            }

            // Get game
            GameData game = gameDAO.getGame(command.getGameID());
            if (game == null) {
                sendError(ctx, "Error: Game not found");
                return;
            }

            String username = auth.username();

            // If they're a player, remove them from the game in database
            if (username.equals(game.whiteUsername())) {
                GameData updatedGame = new GameData(game.gameID(), null, game.blackUsername(),
                        game.gameName(), game.game());
                gameDAO.updateGame(updatedGame);
            } else if (username.equals(game.blackUsername())) {
                GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), null,
                        game.gameName(), game.game());
                gameDAO.updateGame(updatedGame);
            }

            connections.remove(command.getGameID(), ctx);


            NotificationMessage notifyMsg = new NotificationMessage(username + " left the game");
            connections.broadcast(command.getGameID(), notifyMsg, ctx);

        } catch (Exception e) {
            sendError(ctx, "Error: " + e.getMessage());
        }
    }

    private void handleMakeMove(WsMessageContext ctx, UserGameCommand command, String message) {
        try {
            // Validate auth
            AuthData auth = authDAO.getAuth(command.getAuthToken());
            if (auth == null) {
                sendError(ctx, "Error: Invalid auth token");
                return;
            }

            // Get game
            GameData gameData = gameDAO.getGame(command.getGameID());
            if (gameData == null) {
                sendError(ctx, "Error: Game not found");
                return;
            }

            ChessGame game = gameData.game();
            String username = auth.username();

            // Check if game is already over
            if (game.isGameOver()) {
                sendError(ctx, "Error: Game is over");
                return;
            }

            // Verify it's their turn
            ChessGame.TeamColor currentTurn = game.getTeamTurn();
            boolean isWhitePlayer = username.equals(gameData.whiteUsername());
            boolean isBlackPlayer = username.equals(gameData.blackUsername());

            if (!isWhitePlayer && !isBlackPlayer) {
                sendError(ctx, "Error: Observers cannot make moves");
                return;
            }

            if ((currentTurn == ChessGame.TeamColor.WHITE && !isWhitePlayer) ||
                    (currentTurn == ChessGame.TeamColor.BLACK && !isBlackPlayer)) {
                sendError(ctx, "Error: It's not your turn");
                return;
            }

            // Parse as MakeMoveCommand directly from the original message
            MakeMoveCommand moveCommand = gson.fromJson(message, MakeMoveCommand.class);
            ChessMove move = moveCommand.getMove();

            if (move == null) {
                sendError(ctx, "Error: No move provided");
                return;
            }

            try {
                game.makeMove(move);
            } catch (InvalidMoveException e) {
                sendError(ctx, "Error: Invalid move");
                return;
            }

            GameData updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(),
                    gameData.blackUsername(), gameData.gameName(), game);
            gameDAO.updateGame(updatedGameData);

            // Send to client
            LoadGameMessage loadMsg = new LoadGameMessage(game);
            connections.broadcast(command.getGameID(), loadMsg, null);

            // Send move notification to others
            String moveNotification = username + " moved from " + move.getStartPosition() +
                    " to " + move.getEndPosition();
            NotificationMessage moveMsg = new NotificationMessage(moveNotification);
            connections.broadcast(command.getGameID(), moveMsg, ctx);

            // Check for check, checkmate, or stalemate
            ChessGame.TeamColor opponentColor = (currentTurn == ChessGame.TeamColor.WHITE) ?
                    ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

            if (game.isInCheckmate(opponentColor)) {
                String checkmateMsg = opponentColor + " is in checkmate! " + username + " wins!";
                NotificationMessage checkmate = new NotificationMessage(checkmateMsg);
                connections.broadcast(command.getGameID(), checkmate, null);
            } else if (game.isInStalemate(opponentColor)) {
                String stalemateMsg = opponentColor + " is in stalemate. The game is a draw.";
                NotificationMessage stalemate = new NotificationMessage(stalemateMsg);
                connections.broadcast(command.getGameID(), stalemate, null);
            } else if (game.isInCheck(opponentColor)) {
                String checkMsg = opponentColor + " is in check!";
                NotificationMessage check = new NotificationMessage(checkMsg);
                connections.broadcast(command.getGameID(), check, null);
            }

        } catch (Exception e) {
            sendError(ctx, "Error: " + e.getMessage());
        }
    }

    private void handleResign(WsMessageContext ctx, UserGameCommand command) {
        try {
            // Validate auth
            AuthData auth = authDAO.getAuth(command.getAuthToken());
            if (auth == null) {
                sendError(ctx, "Error: Invalid auth token");
                return;
            }

            // Get game
            GameData gameData = gameDAO.getGame(command.getGameID());
            if (gameData == null) {
                sendError(ctx, "Error: Game not found");
                return;
            }

            String username = auth.username();

            // Make sure they're a player (not observer)
            boolean isPlayer = username.equals(gameData.whiteUsername()) ||
                    username.equals(gameData.blackUsername());

            if (!isPlayer) {
                sendError(ctx, "Error: Observers cannot resign");
                return;
            }

            // Check if game is already over
            ChessGame game = gameData.game();
            if (game.isGameOver()) {
                sendError(ctx, "Error: Game is already over");
                return;
            }

            // Mark game as over
            game.setGameOver(true);
            GameData updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(),
                    gameData.blackUsername(), gameData.gameName(), game);
            gameDAO.updateGame(updatedGameData);

            // Send notification to all clients
            String resignMsg = username + " resigned. Game over.";
            NotificationMessage notifyMsg = new NotificationMessage(resignMsg);
            connections.broadcast(command.getGameID(), notifyMsg, null);

        } catch (Exception e) {
            sendError(ctx, "Error: " + e.getMessage());
        }
    }

    private void sendError(WsMessageContext ctx, String errorMessage) {
        try {
            ErrorMessage error = new ErrorMessage(errorMessage);
            connections.sendToSession(ctx, error);
        } catch (Exception e) {
            System.err.println("Error sending error message: " + e.getMessage());
        }
    }
}
