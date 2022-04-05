import concurrent.controller.GUIListener;
import concurrent.controller.Simulator;
import concurrent.view.PrinterView;
import concurrent.view.gui.SimulationView;

/**
 * Bodies simulation - legacy code: sequential, unstructured
 * 
 * @author aricci
 */
public class App {

    public static void main(String[] args) {
                
        PrinterView viewer = new PrinterView();
//    	SimulationView viewer = new SimulationView(620,620);
    	Simulator sim = new Simulator(viewer);
//        viewer.addListener(new GUIListener(sim));
        sim.execute(3);
    }
}
