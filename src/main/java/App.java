import concurrent.controller.Simulator;
import concurrent.controller.ViewListener;
import concurrent.view.View;
import concurrent.view.gui.GUIView;

/*
 * Elaborato di  Elia Marcantognini, Michele Bachetti, Angelo Tinti
 */

public class App {

    public static void main(String[] args) {

        // Start with gui
        View viewer = new GUIView(620, 620);

        // Start with print view
//        View viewer = new PrinterView();

        Simulator sim = new Simulator(viewer);
        viewer.addListener(new ViewListener(sim));
    }
}
