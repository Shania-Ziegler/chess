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

    public void broadcast(Integer gameID, ServerMessage message, Session excludeSession) throws IOException{
    var list = connections.get(gameID);
    if (list != null){
        var removeList = new ArrayList<Connection>();
        for(var conn : list){
            if(conn.session.isOpen()){
                if (!conn.session.equals(excludeSession)) {
                    conn.send(gson.toJson(message));
                }else{
                    removeList.add(conn);
                }
            }
            for (var c:removeList){
                list.remove(c);
            }
        }
    }
    public void sendToSession(Session session, ServerMessage message) throws IOException{
        if(session.isOpen()){
            session.getRemote().sendString(gson.toJson(message));
        }
        }
   private static class Connection {
        public String authToken;
        public Session session;

        public Connection(String authToken, Session session){
            this.authToken = authToken;
            this.session = session;
        }
        public void send(String msg) throws IOException{
            session.getRemote().sendString(msg);
        }
   }
}

