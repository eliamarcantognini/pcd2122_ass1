package concurrent.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIListener implements ActionListener {
    private final Simulator simulator;
    private final Boolean start = true;

    public GUIListener(final Simulator simulator) {
        this.simulator = simulator;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        if (e.getActionCommand() == )

    }

    public Boolean getStart() {
        return start;
    }
}
