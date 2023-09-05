import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class PiStatsClient extends JFrame {

    private JSlider fanSpeedSlider1;
    private JSlider fanSpeedSlider2;
    private JPanel temperatureIndicator1;
    private JPanel temperatureIndicator2;

    private PiStatsServerInterface server;

    public PiStatsClient() {
        setTitle("PiStats Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(300, 300));
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setResizable(false);

        // Create temperature indicators
        temperatureIndicator1 = createTemperatureIndicator();
        temperatureIndicator2 = createTemperatureIndicator();

        // Create fan speed adjustment knobs
        fanSpeedSlider1 = createFanSpeedSlider();
        fanSpeedSlider2 = createFanSpeedSlider();

        // Add components to the frame
        add(new JLabel("Temperature 1:"));
        add(temperatureIndicator1);
        add(fanSpeedSlider1);
        add(new JLabel("Temperature 2:"));
        add(temperatureIndicator2);
        add(fanSpeedSlider2);

        // Connect to the RMI server
        try {
            server = (PiStatsServerInterface) Naming.lookup("rmi://localhost/PiStatsServer");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add listener to fan speed sliders
        fanSpeedSlider1.addChangeListener(e -> {
            double speed = fanSpeedSlider1.getValue();
            System.out.println(speed + " " + fanSpeedSlider1.getValue());
            setFanSpeed(1, speed);
        });

        fanSpeedSlider2.addChangeListener(e -> {
            double speed = fanSpeedSlider2.getValue();
            setFanSpeed(2, speed);
        });

        // Update temperature indicators periodically
        Timer timer = new Timer(500, e -> {
            updateTemperatureIndicators();
        });
        timer.start();

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createTemperatureIndicator() {
        JPanel indicator = new JPanel();
        indicator.setPreferredSize(new Dimension(50, 50));
        indicator.setBackground(Color.GREEN);
        indicator.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return indicator;
    }

    private JSlider createFanSpeedSlider() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        slider.setMajorTickSpacing(25);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }

    private void setFanSpeed(int fanId, double speed) {
        try {
            server.setFanSpeed(fanId, speed);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void updateTemperatureIndicators() {
        try {
            double[] temperature = server.getTemperature();

            updateTemperatureIndicator(temperatureIndicator1, temperature[0]);
            updateTemperatureIndicator(temperatureIndicator2, temperature[1]);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void updateTemperatureIndicator(JPanel indicator, double temperature) {
        indicator.setToolTipText("Temperature: " + temperature + "°C");
        Color color = getTemperatureColor(temperature);
        indicator.setBackground(color);
    }

    private Color getTemperatureColor(double temperature) {
        if (temperature < 60) {
            return Color.GREEN;
        } else if (temperature < 80) {
            return Color.YELLOW;
        } else {
            return Color.RED;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PiStatsClient client = new PiStatsClient();
            client.setVisible(true);
        });
    }
}
