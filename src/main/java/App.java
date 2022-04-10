import concurrent.controller.GUIListener;
import concurrent.controller.Simulator;
import concurrent.view.PrinterView;
import concurrent.view.View;
import concurrent.view.gui.SimulationView;

/**
 * Bodies simulation - legacy code: sequential, unstructured
 * 
 * @author aricci
 */
public class App {

    public static void main(String[] args) {

        // Start with gui
//        View viewer = new SimulationView(620,620);

        // Start with print view
        View viewer = new PrinterView();

        Simulator sim = new Simulator(viewer);
        viewer.addListener(new GUIListener(sim));
    }
}
