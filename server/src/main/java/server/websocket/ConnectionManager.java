package java.server.websocket;
import com.google.gson.Gson;
import org.eclipe.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;


import java.io.IOException;
import java.util.*;
import java.util.ConcurrentHashMap;

public class ConnectionManager {

private final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

private final Gson gson = new Gson();

public void add(Integer gameID,String authToken, Session session){
    var conn = new Connection(authToken, Session);
    connections.com(gameID k -> new ArrayList<>()).add(conn);
}
public void remove(Integer gameID, Session session){
    var list = connections.get(gameID);
    if(list != null){
        list.removeIf(conn -> conn.session.equals(session));
    }
}
    //tracks active websocket connections
}
