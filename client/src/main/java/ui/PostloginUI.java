package ui;

import chess.ChessGame;
import client.Repl;
import client.ServerFacade;
import model.AuthData;
import model.GameData;

public class PostloginUI {
    private final ServerFacade serverFacade;
    private final AuthData authData;
    private ServerFacade.ListGamesResult gamesResult;
    private String serverUrl;

    public PostloginUI(ServerFacade serverFacade, AuthData authData,String serverUrl) {
        this.serverFacade = serverFacade;
        this.authData = authData;
        this.serverUrl = serverUrl;
    }

    public String eval(String cmd, String[] params, Repl repl) {
        try {
            return switch (cmd) {
                case "logout" -> logout(repl);
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "play" -> playGame(params);
                case "observe" -> observeGame(params);
                case "help" -> help();
                default -> help();
            };
        } catch (Exception e) {
            return "Error: " + e.getMessage() + "\n";
        }
    }

    private String logout(Repl repl) throws Exception {
        serverFacade.logout(authData.authToken());
        repl.logout();
        return "Logged out successfully.\n";
    }

    private String createGame(String[] params) throws Exception {
        if (params.length != 1) {
            return "Expected: create <game_name>\n";
        }
        String gameName = params[0];

        serverFacade.createGame(authData.authToken(), gameName);
        return String.format("Created game '%s'.\n", gameName);
    }

    private String listGames() throws Exception {
        gamesResult = serverFacade.listGames(authData.authToken());

        if (gamesResult.games().length == 0) {
            return "No games available.\n";
        }

        StringBuilder result = new StringBuilder("Games:\n");
        // Use .length instead of .size()
        for (int i = 0; i < gamesResult.games().length; i++) {
            // Use array[i] instead of .get(i)
            GameData game = gamesResult.games()[i];
            result.append(String.format("%d. %s (White: %s, Black: %s)\n",
                    i + 1,
                    game.gameName(),
                    game.whiteUsername() != null ? game.whiteUsername() : "available",
                    game.blackUsername() != null ? game.blackUsername() : "available"));
        }
        return result.toString();
    }

    private String playGame(String[] params) throws Exception {
        if (params.length != 2) {
            return "Expected: play <game_number> <WHITE|BLACK>\n";
        }

        int gameNumber;
        try {
            gameNumber = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            return "Game number must be a number\n";
        }

        if (gamesResult == null || gameNumber < 1 || gameNumber > gamesResult.games().length) {
            return "Invalid game number. Use 'list' first.\n";
        }

        String colorStr = params[1].toUpperCase();
        ChessGame.TeamColor color;
        try {
            color = ChessGame.TeamColor.valueOf(colorStr);
        } catch (IllegalArgumentException e) {
            return "Color must be WHITE or BLACK\n";
        }

        GameData game = gamesResult.games()[gameNumber - 1];

        // Join game via HTTP first
        serverFacade.joinGame(authData.authToken(), colorStr, game.gameID());


        try {
            GameplayUI gameplayUI = new GameplayUI(serverUrl, authData.authToken(), game.gameID(), color);
            gameplayUI.run();  // This enters gameplay mode!
            return ""; // Return empty string after gameplay ends
        } catch (Exception e) {
            return "Error connecting to game: " + e.getMessage() + "\n";
        }
    }

    private String observeGame(String[] params) throws Exception {
        if (params.length != 1) {
            return "Expected: observe <game_number>\n";
        }
        int gameNumber;
        try {
            gameNumber = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            return "Game number must be a number\n";
        }
        if (gamesResult == null || gameNumber < 1 || gameNumber > gamesResult.games().length) {
            return "Invalid game number. Use 'list' first.\n";
        }

        GameData game = gamesResult.games()[gameNumber - 1];

        try {
            GameplayUI gameplayUI = new GameplayUI(serverUrl, authData.authToken(), game.gameID(), null);
            gameplayUI.run();
            return "";
        } catch (Exception e) {
            return "Error connecting to game: " + e.getMessage() + "\n";
        }
    }

    private String help() {
        return """
                Available commands:
                  create game_name - Create a new game enter name
                  list - List all games
                  play <game_number> <WHITE|BLACK> - Join a game enter game number and White or Black
                  observe <game_number> - Enter game number to observe a game 
                  logout 
                  help - see commands
                """;
    }
}