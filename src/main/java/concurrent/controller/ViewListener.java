package concurrent.controller;

import java.util.EventListener;
import concurrent.model.Commands;

public class ViewListener implements EventListener {

    private final Simulator simulator;

    public ViewListener(final Simulator simulator) {
        this.simulator = simulator;
    }

    public void eventPerformed(Commands code) {
        switch (code) {
            case START:
                simulator.startSimulation();
                break;
            case STOP:
                simulator.stopSimulation();
                break;
            default:
                break;
        }
    }
}
