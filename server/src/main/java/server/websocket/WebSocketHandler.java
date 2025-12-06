package java.server.websocket;

import com.google.gson.Gson;

//@Websocket
public class WebSocketHandler {
  private final ConnectionManager connections = new ConnectionManager();
  private final Gson gson = new Gson();


    //handles the incoming websocket connections

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("WebSocket connected: " + session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("WebSocket closed: " + session);
    }

}
