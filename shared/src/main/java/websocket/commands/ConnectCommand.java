package websocket.commands;

public class ConnectCommand extends UserGameCommand {
    public ConnectCommand(String authToken, Integer gameID) {
        super(authToken);
        this.commandType = CommandType.CONNECT;
        this.gameID = gameID;
    }
}
