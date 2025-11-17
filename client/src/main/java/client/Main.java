package client;

public class Main {
    public static void main(String[] args) {
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        System.out.println("♕ Welcome to 240 Chess Client ♕");
        new Repl(serverUrl).run();
    }
}