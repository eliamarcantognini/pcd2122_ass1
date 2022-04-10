package concurrent.view;

import concurrent.controller.GUIListener;
import concurrent.model.Body;
import concurrent.model.Boundary;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PrinterView implements View{
    @Override
    public void display(List<Body> bodies, double vt, long iter, Boundary bounds) {
        System.out.println(bodies);
    }

    @Override
    public void setStopEnabled(Boolean enabled) {

    }

    @Override
    public void setStartEnabled(Boolean enabled) {

    }

    @Override
    public void addListener(GUIListener listener) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                listener.eventPerformed("start");
            }
        }, 3000);
    }
}
