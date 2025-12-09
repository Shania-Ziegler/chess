package websocket.commands;

public class LeaveCommand extends UserGameCommand {
public LeaveCommand(String authToken, Integer gameID){
    super(authToken);
    this.commandType= CommandType.LEAVE;
    this.gameID = gameID;
}
}
