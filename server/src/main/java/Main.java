import server.Server;
import dataaccess.DataAccessException;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server(); // may throw DataAccessException
            server.run(8080);

            System.out.println("♕ 240 Chess Server");
        } catch (DataAccessException e) {
            System.err.println("Failed starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
