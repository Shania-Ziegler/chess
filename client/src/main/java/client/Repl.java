package client;

import java.util.Scanner;

public class Repl {
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.println("Welcome to Chess. Type 'help' to get started.");

        String command = "";
        while (!command.equals("quit")) {
            System.out.print(">>> ");
            command = scanner.nextLine();

            String result = eval(command);
            System.out.println(result);
        }
    }

    private String eval(String command) {

        if (command.equals("help")) {
            return """
                help - Display this help message
                quit - Exit the program
                """;
        }
        return "";
    }
}