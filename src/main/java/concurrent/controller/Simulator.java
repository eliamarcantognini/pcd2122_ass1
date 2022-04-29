package concurrent.controller;

import concurrent.model.*;
import concurrent.view.View;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that represents the main controller of the system. Its main responsibility is to manage the
 * Agents of the system, which are represented by the class {@link BodyAgent}.
 */
public class Simulator {

    private final static int BODIES_INIT_WITHOUT_FILE = 500;
    private final static int STEPS_INIT_WITHOUT_FILE = 5000;
    private final static double DT_INIT_WITHOUT_FILE = 0.001;
    private final static Boundary BOUNDARY_INIT_WITHOUT_FILE = new Boundary(-6, -6, 6, 6);
    private final static String CONFIGURATION_FILE_NAME = "config.properties";

//    private final int cores;
//    private CyclicBarrier cyclicBarrier;
    private final View viewer;
    private Context context;
//    private List<BodyAgent> agents;
    private long nSteps;
    /* bodies in the field */
    private BodiesSharedList readSharedList;
    private BodiesSharedList writeSharedList;
    private double vt;
    private long iter;
    private int nBodies;
    private boolean stopFromGUI = false;
    private Configuration configuration;
    private ExecutorService executor;
    private SyncMonitor syncMonitor;

    /**
     * Create the controller and the shared elements in the system, which are the @Context and the CyclicBarrier used
     * for synchronization.
     *
     * @param viewer - the view to be used to display the evolution of the simulation
     */
    public Simulator(View viewer) {

        this.viewer = viewer;
        this.readConfiguration(Simulator.CONFIGURATION_FILE_NAME);
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);
        this.syncMonitor = new SyncMonitor();
//        this.cores = Runtime.getRuntime().availableProcessors();
//        this.cyclicBarrier = new CyclicBarrier(Math.min(this.nBodies, this.cores), () -> {
//            updateSimulation(viewer);
//            if (iter >= nSteps || stopFromGUI) {
//                context.setKeepWorking(false);
//                this.createContext(this.context.getBoundary(), this.context.getDT());
//                this.initSimulation();
//            }
//        });
        this.initSimulation();
        this.viewer.display(readSharedList.getBodies(), vt, iter, context.getBoundary());
    }

    public void exec() {
        while (true) {
            Monitor monitor = new Monitor(readSharedList.getBodies().size());
            try {
                this.syncMonitor.waitBegin();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int iter = 0; iter < nSteps && syncMonitor.shouldContinue(); iter++) {
                for (Body b : readSharedList.getBodies()) {
                    executor.execute(new UpdateTask(b, this.context, monitor));
                }
                try {
                    monitor.awaitCompletion();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                monitor.reset();
                this.updateSimulation();
            }
            this.initSimulation();
        }
    }

    protected void updateSimulation() {
        readSharedList.reset();
        readSharedList.addBodies(writeSharedList.getBodies());
        /* update virtual time */
        vt = vt + this.context.getDT();
        iter++;
        /* display current stage */
        this.viewer.display(readSharedList.getBodies(), vt, iter, context.getBoundary());
    }

    /**
     * Method to call when the simulation has to restart, for example when the button Start from the GUI is
     * pressed. It also waits for the termination of the BodyAgents of the previous simulation, if any.
     */
    public void startSimulation() {
//        waitForAgentsToClose();
//        startAgents();
//        Thread t = new BodyService(nSteps, context, this);
//        t.start();
        syncMonitor.startSimulation();
        viewer.setStartEnabled(false);
        viewer.setStopEnabled(true);
    }

    /**
     * Method to call when the simulation has to stop, for example when the button Stop from the GUI is pressed.
     */
    public void stopSimulation() {
//        this.stopFromGUI = true;
        syncMonitor.stopSimulation();
        viewer.setStopEnabled(false);
    }

    protected void initConfigurationWithoutFile() {
        this.viewer.showMessage("Configuration file not found. Simulation will be initialized with prefixed data: " + Simulator.BODIES_INIT_WITHOUT_FILE + " bodies and " + Simulator.STEPS_INIT_WITHOUT_FILE + " steps.");
        this.context = new Context(Simulator.BOUNDARY_INIT_WITHOUT_FILE, Simulator.DT_INIT_WITHOUT_FILE);
        this.nBodies = Simulator.BODIES_INIT_WITHOUT_FILE;
        this.nSteps = Simulator.STEPS_INIT_WITHOUT_FILE;
    }

    protected void readConfiguration(final String fileConfigurationName) {
        try {
            this.configuration = new Configuration(fileConfigurationName);
            this.initConfigurationWithFile();
        } catch (FileNotFoundException e) {
            this.initConfigurationWithoutFile();
        }
    }

    private void initConfigurationWithFile() {
        Boundary boundary = new Boundary(this.configuration.getLefterBoundary(), this.configuration.getUpperBoundary(), this.configuration.getRighterBoundary(), this.configuration.getLowerBoundary());
        this.createContext(boundary, this.configuration.getDT());
        this.nBodies = this.configuration.getBodiesQuantity();
        this.nSteps = this.configuration.getIterationsQuantity();
    }

    private void createContext(final Boundary boundary, final double dt) {
        this.context = new Context(boundary, dt);
    }

    protected void initSimulation() {
        this.context.restartContext();
        this.stopFromGUI = false;
//        this.agents = new ArrayList<>();
        this.readSharedList = this.context.getReadSharedList();
        this.writeSharedList = this.context.getWriteSharedList();
        createBodies(this.nBodies);
        this.vt = 0;
        this.iter = 0;
//        createAgents();
        viewer.setStopEnabled(false);
        viewer.setStartEnabled(true);
    }

    private void createBodies(final int nBodies) {
        List<Body> bodies = new ArrayList<>();
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < nBodies; i++) {
            double x = context.getBoundary().getX0() * 0.25 + rand.nextDouble() * (context.getBoundary().getX1() - context.getBoundary().getX0()) * 0.25;
            double y = context.getBoundary().getY0() * 0.25 + rand.nextDouble() * (context.getBoundary().getY1() - context.getBoundary().getY0()) * 0.25;
            Body b = new Body(i, new P2d(x, y), new V2d(0, 0), 10);
            bodies.add(b);
        }
        readSharedList.addBodies(bodies);
        writeSharedList.addBodies(readSharedList.getBodies());
    }

//    private void createAgents() {
//        int bodiesPerCore = nBodies / cores + 1;
//        for (int i = 0; i < nBodies; i += bodiesPerCore) {
//            createAgent(i, Math.min(nBodies, i + bodiesPerCore));
//        }
//    }
//
//    private void createAgent(final int startIndex, final int endIndex) {
//        agents.add(new BodyAgent(startIndex, endIndex, this.cyclicBarrier, this.context));
//    }
//
//    private void startAgents() {
//        for (BodyAgent b : agents) {
//            b.start();
//        }
//    }
//
//    private void waitForAgentsToClose() {
//        for (BodyAgent t : agents) {
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
