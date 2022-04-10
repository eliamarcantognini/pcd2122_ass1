package concurrent.controller;

import concurrent.model.*;
import concurrent.view.View;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 * Class that represents the main controller of the system. Its main responsibility is to manage the
 * Agents of the system, which are represented by the class {@link BodyAgent}.
 */
public class Simulator {

    private final static int BODIES_INIT_WITHOUT_FILE = 500;
    private final static int STEPS_INIT_WITHOUT_FILE = 5000;
    private final static double DT_INIT_WITHOUT_FILE = 0.001;
    private final static Boundary BOUNDARY_INIT_WITHOUT_FILE = new Boundary(-6,-6,6,6);
    private final static String CONFIGURATION_FILE_NAME = "config.properties";

    private final int cores;
    private final CyclicBarrier cyclicBarrier;
    private Context context;
    private List<BodyAgent> agents;
    private long nSteps;
    /* bodies in the field */
    private BodiesSharedList readSharedList;
    private BodiesSharedList writeSharedList;

    private double vt;
    private long iter;
    private int nBodies;
    private final View viewer;
    private boolean stopFromGUI = false;
    private Configuration configuration;

    /**
     * Create the controller and the shared elements in the system, which are the @Context and the CyclicBarrier used
     * for synchronization.
     *
     * @param viewer - the view to be used to display the evolution of the simulation
     */
    public Simulator(View viewer) {

        this.viewer = viewer;
        this.readConfiguration(Simulator.CONFIGURATION_FILE_NAME);
        this.cores = Runtime.getRuntime().availableProcessors();
        this.cyclicBarrier = new CyclicBarrier(Math.min(this.nBodies, this.cores), () -> {
            readSharedList.reset();
            readSharedList.addBodies(writeSharedList.getBodies());
            /* update virtual time */
            vt = vt + this.context.getDT();
            iter++;
            /* display current stage */
            viewer.display(readSharedList.getBodies(), vt, iter, context.getBoundary());
            if (iter >= nSteps || stopFromGUI) {
                context.setKeepWorking(false);
                this.createContext(this.context.getBoundary(), this.context.getDT());
                this.initSimulation();
            }
        });
        this.initSimulation();

    }

    /**
     * Method to call when the simulation has to restart, for example when the button Start from the GUI is
     * pressed.
     */
    public void startSimulation() {
        startAgents();
        viewer.setStartEnabled(false);
        viewer.setStopEnabled(true);
    }

    /**
     * Method to call when the simulation has to stop, for example when the button Stop from the GUI is pressed.
     */
    public void stopSimulation() {
        this.stopFromGUI = true;
        viewer.setStopEnabled(false);
    }

    protected void initConfigurationWithoutFile() {
        this.viewer.showMessage("Configuration file not found. Simulation will be initialized with prefixed data: "
                + Simulator.BODIES_INIT_WITHOUT_FILE + " bodies and "
                + Simulator.STEPS_INIT_WITHOUT_FILE + " steps.");
        this.context = new Context(Simulator.BOUNDARY_INIT_WITHOUT_FILE,Simulator.DT_INIT_WITHOUT_FILE);
        this.nBodies = Simulator.BODIES_INIT_WITHOUT_FILE;
        this.nSteps = Simulator.STEPS_INIT_WITHOUT_FILE;
    }


    protected void readConfiguration(final String fileConfigurationName){
        try {
            this.configuration = new Configuration(fileConfigurationName);
            this.initConfigurationWithFile();
        } catch (FileNotFoundException e) {
            this.initConfigurationWithoutFile();
        }
    }

    private void initConfigurationWithFile(){
        Boundary boundary = new Boundary(this.configuration.getLefterBoundary(),
            this.configuration.getUpperBoundary(),
            this.configuration.getRighterBoundary(),
            this.configuration.getLowerBoundary());
        this.createContext(boundary,this.configuration.getDT());
        this.nBodies = this.configuration.getBodiesQuantity();
        this.nSteps = this.configuration.getIterationsQuantity();
    }

    private void createContext(final Boundary boundary, final double dt){
        this.context = new Context(boundary,dt);
    }

    private void initSimulation() {
        this.stopFromGUI = false;
        this.agents = new ArrayList<>();
        this.readSharedList = this.context.getReadSharedList();
        this.writeSharedList = this.context.getWriteSharedList();
        createBodies(this.nBodies);
        this.vt = 0;
        this.iter = 0;
        createAgents();
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

    private void createAgents() {
        if (nBodies < cores) {
            for (int i = 0; i < nBodies; i++) {
                createAgent(i, i + 1);
            }
        } else {
            int bodiesPerCore = nBodies / cores;
            createAgent(0, bodiesPerCore);
            for (int i = 1; i < cores - 1; i++) {
                createAgent(i * bodiesPerCore, i * bodiesPerCore + bodiesPerCore);
            }
            createAgent(bodiesPerCore * (cores - 1), this.readSharedList.getBodies().size());
        }
    }

    private void createAgent(final int startIndex, final int endIndex) {
        agents.add(new BodyAgent(startIndex, endIndex, this.cyclicBarrier, this.context));
    }

    private void startAgents() {
        for (BodyAgent b : agents) {
            b.start();
        }
    }
}
