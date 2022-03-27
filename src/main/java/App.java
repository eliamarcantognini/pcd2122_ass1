import concurrent.controller.Simulator;
import concurrent.view.PrinterView;
//import concurrent.view.SimulationView;

/**
 * Bodies simulation - legacy code: sequential, unstructured
 * 
 * @author aricci
 */
public class App {

    public static void main(String[] args) {
                
//    	SimulationView viewer = new SimulationView(620,620);
        PrinterView viewer = new PrinterView();
    	Simulator sim = new Simulator(viewer);
        sim.execute(500000);
    }
}
