package concurrent.view;

import concurrent.model.Body;
import concurrent.model.Boundary;

import java.util.ArrayList;
import java.util.List;

public class PrinterView implements View{
    @Override
    public void display(List<Body> bodies, double vt, long iter, Boundary bounds) {
    }

    @Override
    public void setStopEnabled(Boolean enabled) {

    }

    @Override
    public void setStartEnabled(Boolean enabled) {

    }
}
