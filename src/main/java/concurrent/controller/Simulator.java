package concurrent.controller;

import concurrent.model.*;
import concurrent.view.SimulationView;

import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class Simulator {

	private final SimulationView viewer;

	/* bodies in the field */
	ArrayList<Body> readBodies;

	/* boundary of the field */
	private Boundary bounds;

	private CyclicBarrier cyclicBarrier;

	/* virtual time step */
	double dt;

	public Simulator(SimulationView viewer) {
		this.viewer = viewer;

		/* initializing boundary and bodies */

		// testBodySet1_two_bodies();
		// testBodySet2_three_bodies();
		// testBodySet3_some_bodies();
		testBodySet4_many_bodies();
	}
	
	public void execute(long nSteps) {

		/* init virtual time */

		/* virtual time */
		double vt = 0;
		dt = 0.001;

		long iter = 0;

		/* simulation loop */

		while (iter < nSteps) {

			/* update bodies velocity */

			for (Body b : readBodies) {
				/* compute total force on bodies */
				V2d totalForce = computeTotalForceOnBody(b);

				/* compute instant acceleration */
				V2d acc = new V2d(totalForce).scalarMul(1.0 / b.getMass());

				/* update velocity */
				b.updateVelocity(acc, dt);
			}

			/* compute bodies new pos */

			for (Body b : readBodies) {
				b.updatePos(dt);
			}

			/* check collisions with boundaries */

			for (Body b : readBodies) {
				b.checkAndSolveBoundaryCollision(bounds);
			}

			/* update virtual time */

			vt = vt + dt;
			iter++;

			/* display current stage */

			viewer.display(readBodies, vt, iter, bounds);

		}
	}

	private V2d computeTotalForceOnBody(Body b) {

		V2d totalForce = new V2d(0, 0);

		/* compute total repulsive force */

		for (Body otherBody : readBodies) {
			if (!b.equals(otherBody)) {
				try {
					V2d forceByOtherBody = b.computeRepulsiveForceBy(otherBody);
					totalForce.sum(forceByOtherBody);
				} catch (Exception ignored) {
				}
			}
		}

		/* add friction force */
		totalForce.sum(b.getCurrentFrictionForce());

		return totalForce;
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
		readBodies = new ArrayList<Body>();
		this.cyclicBarrier = new CyclicBarrier(nBodies, () -> System.out.println("Prova CB"));
		for (int i = 0; i < nBodies; i++) {
			double x = bounds.getX0()*0.25 + rand.nextDouble() * (bounds.getX1() - bounds.getX0()) * 0.25;
			double y = bounds.getY0()*0.25 + rand.nextDouble() * (bounds.getY1() - bounds.getY0()) * 0.25;
			Body b = new Body(i, new P2d(x, y), new V2d(0, 0), 10);
			readBodies.add(b);
			new BodyAgent(b,this.readBodies,this.cyclicBarrier).start();
//			bA.start();
		}
	}

	private void testBodySet4_many_bodies() {
		bounds = new Boundary(-6.0, -6.0, 6.0, 6.0);
		int nBodies = 8;
//		int nBodies = 1000;
		createBodies(nBodies);
	}

}
