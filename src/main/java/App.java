import async.controller.Simulator;
import async.controller.ViewListener;
import async.view.PrinterView;
import async.view.View;
import async.view.gui.GUIView;

/*
 * Elaborato di Elia Marcantognini, Michele Bachetti, Angelo Tinti
 */

public class App {

    public static void main(String[] args) {

        int nBodies = Integer.parseInt(args[0]);
        int nSteps = Integer.parseInt(args[1]);
        View viewer = new PrinterView();

        Simulator sim = new Simulator(viewer, nBodies, nSteps);
        viewer.addListener(new ViewListener(sim));
    }
}
