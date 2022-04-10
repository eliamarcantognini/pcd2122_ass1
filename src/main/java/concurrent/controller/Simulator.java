package concurrent.controller;

import concurrent.model.*;
import concurrent.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 * Class that represents the main controller of the system. Its main responsibility is to manage the
 * Agents of the system, which are represented by the class {@link BodyAgent}.
 */
public class Simulator {

    private final int cores;
    private final CyclicBarrier cyclicBarrier;
    private Context context;
    private List<BodyAgent> agents;
    private final long nSteps;
    /* bodies in the field */
    private BodiesSharedList readSharedList;
    private BodiesSharedList writeSharedList;

    private double vt;
    private long iter;
    private final int nBodies;
    private final View viewer;
    private boolean stopFromGUI = false;

    /**
     * Create the controller and the shared elements in the system, which are the @Context and the CyclicBarrier used
     * for synchronization.
     *
     * @param viewer - the view to be used to display the evolution of the simulation
     */
    public Simulator(View viewer) {

        this.nBodies = 10;
        this.nSteps = 5000;
        this.cores = Runtime.getRuntime().availableProcessors();
        this.viewer = viewer;

        this.cyclicBarrier = new CyclicBarrier(Math.min(this.nBodies, this.cores), () -> {
            readSharedList.reset();
            readSharedList.addBodies(writeSharedList.getBodies());
            /* update virtual time */
            vt = vt + Context.DT;
            iter++;
            /* display current stage */
            viewer.display(readSharedList.getBodies(), vt, iter, context.getBoundary());
            if (iter >= nSteps || stopFromGUI) {
                context.setKeepWorking(false);
                initSimulation();
            }
        });

        initSimulation();

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

    private void initSimulation() {
        this.stopFromGUI = false;
        this.context = new Context();
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
        int bodiesPerCore = nBodies / cores+1;
        for(int i = 0; i < nBodies; i += bodiesPerCore) {
            createAgent(i, Math.min(nBodies, i+bodiesPerCore));
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
