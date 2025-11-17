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

    public PostloginUI(ServerFacade serverFacade, AuthData authData) {
        this.serverFacade = serverFacade;
        this.authData = authData;
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
            return "Expected: play game_number <WHITE|BLACK>\n";
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


        serverFacade.joinGame(authData.authToken(), colorStr, game.gameID());


        System.out.println("\nWhite's Perspective");
        BoardDrawer.drawBoard(game.game(), ChessGame.TeamColor.WHITE);

        System.out.println("\nBlack's Perspective");
        BoardDrawer.drawBoard(game.game(), ChessGame.TeamColor.BLACK);

        return String.format("\nJoined game '%s' as %s.\n", game.gameName(), colorStr);
    }

    private String observeGame(String[] params) throws Exception {
        if (params.length != 1) {
            return "Expected: observe game_number\n";
        }

        int gameNumber;
        try {
            gameNumber = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            return "Game number must be a number hint: check list for number\n";
        }


        if (gamesResult == null || gameNumber < 1 || gameNumber > gamesResult.games().length) {
            return "Invalid game number. Use 'list' first.\n";
        }

        String colorStr = params[1].toUpperCase();
        ChessGame.TeamColor color;
        try {
            color = ChessGame.TeamColor.valueOf(colorStr);
        }catch(IllegalArgumentException e){
            return "ONLY WHITE OR BLACK CHESS PEICES";
        }
        GameData game = gamesResult.games()[gameNumber - 1];

        try {
            serverFacade.joinGame(authData.authToken(), colorStr, game.gameID());
        } catch (Exception e) {
            // Check if it's a 403 error (already taken)
            if (e.getMessage().contains("403")) {
                return String.format("Error: The %s position in '%s' is already taken.\n",
                        colorStr, game.gameName());
            }
            throw e; // Re-throw other errors
        }


        game = gamesResult.games()[gameNumber - 1];


        serverFacade.joinGame(authData.authToken(), null, game.gameID());


        System.out.println("\n White's Perspective");
        BoardDrawer.drawBoard(game.game(), ChessGame.TeamColor.WHITE);

        System.out.println("\nBlack's Perspective");
        BoardDrawer.drawBoard(game.game(), ChessGame.TeamColor.BLACK);

        return String.format("\nObserving game '%s'.\n", game.gameName());
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