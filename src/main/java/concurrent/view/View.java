package concurrent.view;

import concurrent.model.Body;
import concurrent.model.Boundary;

import java.util.ArrayList;

public interface View {
    void display(ArrayList<Body> bodies, double vt, long iter, Boundary bounds);
}
