package concurrent.view;

import concurrent.controller.GUIListener;
import concurrent.model.Body;
import concurrent.model.Boundary;

import java.util.ArrayList;
import java.util.List;

public interface View {
    void display(List<Body> bodies, double vt, long iter, Boundary bounds);
    void setStopEnabled(final Boolean enabled);
    void setStartEnabled(final Boolean enabled);
    void addListener(final GUIListener listener);
}
