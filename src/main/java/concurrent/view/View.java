package concurrent.view;

import concurrent.model.Body;
import concurrent.model.Boundary;

import java.util.ArrayList;
import java.util.List;

public interface View {
    void display(List<Body> bodies, double vt, long iter, Boundary bounds);
}
