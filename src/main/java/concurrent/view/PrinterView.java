package concurrent.view;

import concurrent.model.Body;
import concurrent.model.Boundary;

import java.util.ArrayList;

public class PrinterView implements View{
    @Override
    public void display(ArrayList<Body> bodies, double vt, long iter, Boundary bounds) {
        System.out.println(iter);
    }
}
