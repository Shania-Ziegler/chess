package ui;

import client.Repl;
import client.ServerFacade;
import model.AuthData;
import java.util.Arrays;

public class PreloginUI {
    private final ServerFacade serverFacade;

    public PreloginUI(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
    }

    public String eval(String cmd, String[] params, Repl repl) {
        try {
            return switch (cmd) {
                case "register" -> register(params, repl);
                case "login" -> login(params, repl);
                case "quit" -> quit(repl);
                case "help" -> help();
                default -> "Unknown command type help for list of commands";
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String register(String[] params, Repl repl) throws Exception {
        if (params.length != 3) {
            return "Error - Expected: register <username> <password> <email>";
        }
        String username = params[0];
        String password = params[1];
        String email = params[2];

        AuthData authData = serverFacade.register(username, password, email);
        repl.setAuthData(authData);
        return String.format("Registered and logged in as %s.\n", authData.username());
    }

    private String login(String[] params, Repl repl) throws Exception {
        if (params.length != 2) {
            return "Error: Expected: login <username> <password>";
        }
        String username = params[0];
        String password = params[1];

        AuthData authData = serverFacade.login(username, password);
        repl.setAuthData(authData);
        return String.format("Logged in as %s.\n", authData.username());
    }

    private String quit(Repl repl) {
        repl.quit();
        return "Goodbye!";
    }

    private String help() {
        return """
                Available commands:
                  register <username> <password> <email> - Create a new account
                  login <username> <password> - Login to account
                  quit - Exit the program
                  help
                """;
    }
}