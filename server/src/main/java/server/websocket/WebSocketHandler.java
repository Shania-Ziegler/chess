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
        System.out.println("WebSocket connected: " + ctx.getSessionId());
    }

    public void onMessage(WsMessageContext ctx) {
        try {
            String message = ctx.message();
            System.out.println("Received: " + message);

            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

            switch (command.getCommandType()) {
                case CONNECT -> handleConnect(ctx, command);
                case MAKE_MOVE -> handleMakeMove(ctx, command);
                case LEAVE -> handleLeave(ctx, command);
                case RESIGN -> handleResign(ctx, command);
            }
        } catch (Exception e) {
            sendError(ctx, "Error: " + e.getMessage());
        }
    }

    private void handleConnect(WsMessageContext ctx, UserGameCommand command) {
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
        // TODO: Implement
        sendError(ctx, "Error: LEAVE not yet implemented");
    }

    private void handleMakeMove(WsMessageContext ctx, UserGameCommand command) {
        // TODO: Implement
        sendError(ctx, "Error: MAKE_MOVE not yet implemented");
    }

    private void handleResign(WsMessageContext ctx, UserGameCommand command) {
        // TODO: Implement
        sendError(ctx, "Error: RESIGN not yet implemented");
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
