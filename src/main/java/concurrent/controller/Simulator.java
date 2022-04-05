package concurrent.controller;

import concurrent.model.*;
import concurrent.view.View;
import gov.nasa.jpf.vm.Verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Simulator {

    private Context context;

    private final int cores;

    /* bodies in the field */
    private List<Body> readBodies;

    private List<BodyAgent> agents;

    /* boundary of the field */
    private long nSteps;

    private final CyclicBarrier cyclicBarrier;

    private BodiesSharedList sharedList;

    private double vt;
    private long iter;
    private final int nBodies;
    private final View viewer;
    private boolean stopFromGUI = false;

    public Simulator(View viewer) {

        this.nBodies = 5;
        this.cores = Runtime.getRuntime().availableProcessors();
        this.viewer = viewer;
        this.context = new Context();
        this.readBodies = new ArrayList<>();
        this.agents = new ArrayList<>();
        this.sharedList = this.context.getSharedList();

        this.cyclicBarrier = new CyclicBarrier(Math.min(this.nBodies, this.cores), () -> {
            this.readBodies = sharedList.getBodies();
            /* update virtual time */
            vt = vt + Context.DT;
            iter++;
            /* display current stage */
            viewer.display(readBodies, vt, iter, context.getBoundary());
            if (iter >= nSteps || stopFromGUI) {
                Verify.beginAtomic();
                context.setKeepWorking(false);
                Verify.endAtomic();
//                initSimulation();
            }
        });

        createBodies(nBodies);
    }

    private void initSimulation() {
        this.stopFromGUI = false;
        this.context = new Context();
        this.readBodies = new ArrayList<>();
        this.agents = new ArrayList<>();
        this.sharedList = this.context.getSharedList();

        createBodies(this.nBodies);
        execute(this.nSteps);
        viewer.setStopEnabled(false);
        viewer.setStartEnabled(true);
    }

    public void execute(long nSteps) {
        this.nSteps = nSteps;
        /* virtual time */
        this.vt = 0;
        this.iter = 0;
        createAgents();
        startAgents();
    }

    private void createBodies(final int nBodies) {
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < nBodies; i++) {
            double x = context.getBoundary().getX0() * 0.25 + rand.nextDouble() * (context.getBoundary().getX1() - context.getBoundary().getX0()) * 0.25;
            double y = context.getBoundary().getY0() * 0.25 + rand.nextDouble() * (context.getBoundary().getY1() - context.getBoundary().getY0()) * 0.25;
            Body b = new Body(i, new P2d(x, y), new V2d(0, 0), 10);
            readBodies.add(new Body(b));
        }
        sharedList.addBodies(readBodies);
    }

    private void createAgents(){
        if (nBodies < cores) {
            for (int i = 0; i < nBodies; i++) {
                createAgent(i, i+1);
            }
        } else {
            int bodiesPerCore = nBodies / cores;
            createAgent(0, bodiesPerCore);
            for (int i = 1; i < cores - 1; i++) {
                createAgent(i * bodiesPerCore, i * bodiesPerCore + bodiesPerCore);
            }
            createAgent(bodiesPerCore * (cores - 1), this.readBodies.size());
        }
    }

    private void createAgent(final int startIndex, final int endIndex) {
        agents.add(new BodyAgent(startIndex, endIndex, this.readBodies, this.cyclicBarrier, this.sharedList, this.context));
    }

    private void startAgents() {
        for (BodyAgent b: agents) {
            b.start();
        }
    }

    public void startSimulation() {
        startAgents();
        viewer.setStartEnabled(false);
        viewer.setStopEnabled(true);
    }

    public void stopSimulation() {
        this.stopFromGUI = true;
        viewer.setStopEnabled(false);
    }

}
