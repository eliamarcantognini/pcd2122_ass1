package concurrent.controller;
import java.util.EventListener;

public class ViewListener implements EventListener {
    private final Simulator simulator;

    public ViewListener(final Simulator simulator) {
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
