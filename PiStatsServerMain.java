import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class PiStatsServerMain {
    public static void main(String[] args) {
        try {
            // Create an instance of the server implementation
            PiStatsServer server = new PiStatsServer();

            // Bind the server object to the RMI registry
            LocateRegistry.createRegistry(1099);
            Naming.rebind("PiStatsServer", server);

            System.out.println("PiStatsServer is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
