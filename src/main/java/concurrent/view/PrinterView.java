package concurrent.view;

import concurrent.controller.ViewListener;
import concurrent.model.Body;
import concurrent.model.Boundary;
import concurrent.model.Commands;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PrinterView implements View {
    @Override
    public void display(final List<Body> bodies, final double vt, final long iter, final Boundary bounds) {
//        System.out.println(bodies);
    }

    @Override
    public void setStopEnabled(final Boolean enabled) {
    }

    @Override
    public void setStartEnabled(final Boolean enabled) {
    }

    @Override
    public void addListener(final ViewListener listener) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                listener.eventPerformed(Commands.START);
            }
        }, 0);
    }

    @Override
    public void showMessage(final String message) {
        System.out.println("Alert: " + message);
    }

}
