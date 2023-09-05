import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PiStatsServer extends UnicastRemoteObject implements PiStatsServerInterface {
    private static final String TEMPERATURE_FILE_PATH = "temperature.txt";

    private static final String SPEED_FILE_PATH1 = "speed1.txt";
    private static final String SPEED_FILE_PATH2 = "speed2.txt";

    public PiStatsServer() throws RemoteException {
        super();
    }

    @Override
    public double[] getTemperature() throws RemoteException {
        try {
            double[] temperature = new double[2];
            BufferedReader reader = new BufferedReader(new FileReader(TEMPERATURE_FILE_PATH));
            String temperatureString = reader.readLine();
            String[] temperatureStrings = temperatureString.split(" ");
            temperature[0] = Double.parseDouble(temperatureStrings[0]);
            temperature[1] = Double.parseDouble(temperatureStrings[1]);
            System.out.println("Temperature: " + temperature[0] + " " + temperature[1]);
            reader.close();
            return temperature;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setFanSpeed(int id, double speed) throws RemoteException {
        try {
            
            // select the file to write to
            String chosenfan = "";
            if (id == 1) {
                chosenfan = SPEED_FILE_PATH1;
            } else if (id == 2) {
                chosenfan = SPEED_FILE_PATH2;
            } else {
                System.out.println("Invalid fan id");
                return;
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(chosenfan));
            writer.write(String.valueOf(speed));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
