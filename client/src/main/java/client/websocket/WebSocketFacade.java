package client.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.*;
import websocket.messages.*;


import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class WebSocketFacade extends Endpoint {
    private Session session;
    private final Gson gson = new Gson();
    private final NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler handler) throws Exception {
        this.notificationHandler = handler;
        URI uri = new URI(url.replace("http://", "ws://").replace("https://", "wss://"));
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {

            @Override
            public void onMessage(String message) {
                try {
                    ServerMessage serverMsg = gson.fromJson(message, ServerMessage.class);

                    switch (serverMsg.getServerMessageType()) {
                        case LOAD_GAME -> {
                            LoadGameMessage loadMsg = gson.fromJson(message, LoadGameMessage.class);
                            notificationHandler.notify(loadMsg);
                        }
                        case ERROR -> {
                            ErrorMessage errorMsg = gson.fromJson(message, ErrorMessage.class);
                            notificationHandler.notify(errorMsg);
                        }
                        case NOTIFICATION -> {
                            NotificationMessage notifyMsg = gson.fromJson(message, NotificationMessage.class);
                            notificationHandler.notify(notifyMsg);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error processing message: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("WebSocket connection opened");
    }

    public void connect(String authToken, int gameID) throws IOException {
        ConnectCommand cmd = new ConnectCommand(authToken, gameID);
        send(gson.toJson(cmd));
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws IOException {
        MakeMoveCommand cmd = new MakeMoveCommand(authToken, gameID, move);
        send(gson.toJson(cmd));
    }

    public void leave(String authToken, int gameID) throws IOException {
        LeaveCommand cmd = new LeaveCommand(authToken, gameID);
        send(gson.toJson(cmd));
    }

    public void resign(String authToken, int gameID) throws IOException {
        ResignCommand cmd = new ResignCommand(authToken, gameID);
        send(gson.toJson(cmd));
    }

    private void send(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public void close() throws IOException {
        this.session.close();
    }
}