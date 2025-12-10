package server.websocket;

import com.google.gson.Gson;
import io.javalin.websocket.WsContext;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    private final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    public void add(Integer gameID, String authToken, WsContext session) {
        var conn = new Connection(authToken, session);
        connections.computeIfAbsent(gameID, k -> new ArrayList<>()).add(conn);
    }

    public void remove(Integer gameID, WsContext session) {
        var list = connections.get(gameID);
        if (list != null) {
            list.removeIf(conn -> conn.session.equals(session));
        }
    }

    public void broadcast(Integer gameID, ServerMessage message, WsContext excludeSession) {
        var list = connections.get(gameID);
        if (list != null) {
            for (var conn : list) {
                if (excludeSession == null || !conn.session.equals(excludeSession)) {
                    conn.send(gson.toJson(message));
                }
            }
        }
    }

    public void sendToSession(WsContext session, ServerMessage message) {
        session.send(gson.toJson(message));
    }

    private static class Connection {
        public String authToken;
        public WsContext session;

        public Connection(String authToken, WsContext session) {
            this.authToken = authToken;
            this.session = session;
        }

        public void send(String msg) {
            try {
                session.send(msg);
            } catch (Exception e) {
                System.out.println("Failed to send to closed connection: " + e.getMessage());
            }
        }
    }
}