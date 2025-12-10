package client;

import java.util.Scanner;
import model.AuthData;
import ui.PreloginUI;
import ui.PostloginUI;
import static ui.EscapeSequences.*;

public class Repl {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private State state = State.LOGGED_OUT;
    private AuthData authData;

    private PreloginUI preloginUI;
    private PostloginUI postloginUI;

    private enum State {
        LOGGED_OUT,
        LOGGED_IN,
        QUIT
    }

    public Repl(String serverUrl) {
        this.serverFacade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.preloginUI = new PreloginUI(serverFacade);
    }

    public void run() {
        System.out.println("Welcome to Chess 240 ♔: Type 'help' for commands.");

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (state != State.QUIT) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Exception e) {
                System.out.print(SET_TEXT_COLOR_RED + "Error: " + e.getMessage() + "\n");
            }
            System.out.print(RESET_TEXT_COLOR);
        }
        System.out.println("End");
    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_WHITE);
        if (state == State.LOGGED_IN) {
            System.out.print("[LOGGED_IN] >>> ");
        } else {
            System.out.print("[LOGGED_OUT] >>> ");
        }
        System.out.print(RESET_TEXT_COLOR);
    }

    private String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = new String[tokens.length - 1];
            System.arraycopy(tokens, 1, params, 0, params.length);

            if (state == State.LOGGED_OUT) {
                return preloginUI.eval(cmd, params, this);
            } else {
                return postloginUI.eval(cmd, params, this);
            }
        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }

    // Methods called by UI classes
    public void setAuthData(AuthData authData) {
        this.authData = authData;
        this.state = State.LOGGED_IN;
        // Create postloginUI when logging in
        this.postloginUI = new PostloginUI(serverFacade, authData);
    }

    public void logout() {
        this.authData = null;
        this.state = State.LOGGED_OUT;
        this.postloginUI = null; // Clear the UI
    }

    public void quit() {
        this.state = State.QUIT;
    }
}