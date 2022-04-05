import concurrent.controller.GUIListener;
import concurrent.controller.Simulator;
import concurrent.view.PrinterView;


/**
 * Bodies simulation - legacy code: sequential, unstructured
 * 
 * @author aricci
 */
public class App {

    public static void main(String[] args) {

        int nBodies = Integer.parseInt(args[0]);
        int nSteps = Integer.parseInt(args[1]);
        int mode = Integer.parseInt(args[2]); // 0 scalable, 1 full thread

        PrinterView viewer = new PrinterView();
        Simulator sim = new Simulator(viewer, nBodies, mode);
        sim.execute(nSteps);
    }
}
