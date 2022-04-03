package concurrent.controller;

import concurrent.model.*;
import concurrent.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Simulator {

    private final Context context;

    private final int cores;

    /* bodies in the field */
    List<Body> readBodies;

    /* boundary of the field */
    private long nSteps;

    private final CyclicBarrier cyclicBarrier;

    private final BodiesSharedList sharedList;

    private double vt;
    private long iter;
    private final int nBodies;

    public Simulator(View viewer) {

        this.context = new Context();
        this.nBodies = 10000;
        this.cores = Runtime.getRuntime().availableProcessors();
        readBodies = new ArrayList<>();
        this.sharedList = this.context.getSharedList();

        this.cyclicBarrier = new CyclicBarrier(Math.min(this.nBodies, this.cores), () -> {
            this.readBodies = sharedList.getBodies();
            /* update virtual time */
            vt = vt + Context.DT;
            iter++;
            /* display current stage */
            viewer.display((ArrayList<Body>) readBodies, vt, iter, context.getBoundary());
            if (iter >= nSteps)
                context.setKeepWorking(false);
        });


        createBodies(nBodies);
    }

    public void execute(long nSteps) {
        this.nSteps = nSteps;
        /* virtual time */
        this.vt = 0;
        this.iter = 0;

        if (nBodies < cores) {
			for (int i = 0; i < nBodies; i++) {
                createAndStartAgent(i, i+1);
			}
        } else {
        	int bodiesPerCore = nBodies / cores;
            createAndStartAgent(0, bodiesPerCore);
            for (int i = 1; i < cores - 1; i++) {
                createAndStartAgent(i * bodiesPerCore, i * bodiesPerCore + bodiesPerCore);
            }
            createAndStartAgent(bodiesPerCore * (cores - 1), this.readBodies.size());
        }
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

    private void createAndStartAgent(final int startIndex, final int endIndex) {
        new BodyAgent(startIndex, endIndex, this.readBodies, this.cyclicBarrier, this.sharedList, this.context).start();
    }

}
