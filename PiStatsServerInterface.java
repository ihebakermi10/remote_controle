import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PiStatsServerInterface extends Remote {
   
    double[] getTemperature() throws RemoteException;
    void setFanSpeed(int id,double speed) throws RemoteException;
}
