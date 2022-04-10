package concurrent.controller;
import java.util.EventListener;

public class GUIListener implements EventListener {
    private final Simulator simulator;

    public GUIListener(final Simulator simulator) {
        this.simulator = simulator;
    }

    public void eventPerformed(String code) {
        switch (code) {
            case "start":
                simulator.startSimulation();
                break;
            case "stop":
                simulator.stopSimulation();
                break;
            default:
                break;
        }
    }
}
