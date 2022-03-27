package concurrent.controller;

import concurrent.model.*;
import concurrent.view.SimulationView;
import concurrent.view.View;

import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class Simulator {

	public final static double DT = 0.001;
	public final static Boundary BOUNDS = new Boundary(-6.0, -6.0, 6.0, 6.0);
	public static boolean keepWorking = true;

	private final View viewer;

	/* bodies in the field */
	List<Body> readBodies;

	/* boundary of the field */
	private Boundary bounds;
	private final long nSteps = 50000;

	private final CyclicBarrier cyclicBarrier;

	private SyncList monitorList;

	private double vt;
	private long iter;

	public Simulator(View viewer) {
		this.viewer = viewer;

		int nBodies = 10;

		readBodies = new ArrayList<>();

		this.cyclicBarrier = new CyclicBarrier(nBodies, () -> {
			this.readBodies = monitorList.getBodies();
			monitorList.reset();

			/* update virtual time */

			vt = vt + DT;
			iter++;

			/* display current stage */

			viewer.display((ArrayList<Body>) readBodies, vt, iter, BOUNDS);
			if (iter > nSteps)
				keepWorking = false;
		});

		this.monitorList = new SyncList();

		createBodies(nBodies);
	}
	
	public void execute(long nSteps) {

//		this.nSteps = nSteps;

		/* init virtual time */

		/* virtual time */
		this.vt = 0;

		this.iter = 0;

		for (Body b: readBodies) {
			new BodyAgent(b, this.readBodies, this.cyclicBarrier, this.monitorList).start();
		}
	}
	
	private void testBodySet1_two_bodies() {
		bounds = new Boundary(-4.0, -4.0, 4.0, 4.0);
		readBodies = new ArrayList<Body>();
		readBodies.add(new Body(0, new P2d(-0.1, 0), new V2d(0,0), 1));
		readBodies.add(new Body(1, new P2d(0.1, 0), new V2d(0,0), 2));
	}

	private void testBodySet2_three_bodies() {
		bounds = new Boundary(-1.0, -1.0, 1.0, 1.0);
		readBodies = new ArrayList<Body>();
		readBodies.add(new Body(0, new P2d(0, 0), new V2d(0,0), 10));
		readBodies.add(new Body(1, new P2d(0.2, 0), new V2d(0,0), 1));
		readBodies.add(new Body(2, new P2d(-0.2, 0), new V2d(0,0), 1));
	}

	private void testBodySet3_some_bodies() {
		bounds = new Boundary(-4.0, -4.0, 4.0, 4.0);
		int nBodies = 100;
		createBodies(nBodies);
	}

	private void createBodies(final int nBodies) {
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < nBodies; i++) {
			double x = BOUNDS.getX0()*0.25 + rand.nextDouble() * (BOUNDS.getX1() - BOUNDS.getX0()) * 0.25;
			double y = BOUNDS.getY0()*0.25 + rand.nextDouble() * (BOUNDS.getY1() - BOUNDS.getY0()) * 0.25;
			Body b = new Body(i, new P2d(x, y), new V2d(0, 0), 10);
			readBodies.add(new Body(b));
		}
	}

	private void testBodySet4_many_bodies() {
		bounds = new Boundary(-6.0, -6.0, 6.0, 6.0);
		int nBodies = 3;
//		int nBodies = 1000;
		createBodies(nBodies);
	}

}
