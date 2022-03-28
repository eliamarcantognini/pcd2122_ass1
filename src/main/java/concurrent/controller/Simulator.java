package concurrent.controller;

import concurrent.model.*;
import concurrent.view.View;

import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class Simulator {

	private final Context context;

	private final View viewer;
	private final int cores;

	/* bodies in the field */
	List<Body> readBodies;

	/* boundary of the field */
	private long nSteps;

	private final CyclicBarrier cyclicBarrier;

	private SharedList sharedList;

	private double vt;
	private long iter;
	private final int nBodies;

	public Simulator(View viewer) {

		this.context = new Context();
		this.viewer = viewer;
		this.nBodies = 100;
		this.cores = Runtime.getRuntime().availableProcessors();
		readBodies = new ArrayList<>();

		this.cyclicBarrier = new CyclicBarrier(this.cores, () -> {
			this.readBodies = sharedList.getBodies();

			/* update virtual time */

			vt = vt + Context.DT;
			iter++;

			/* display current stage */

			viewer.display((ArrayList<Body>) readBodies, vt, iter, context.getBoundary());
			if (iter >= nSteps)
				context.setKeepWorking(false);
		});

		this.sharedList = new SharedList();

		createBodies(nBodies);
	}
	
	public void execute(long nSteps) {

		this.nSteps = nSteps;

		/* init virtual time */

		/* virtual time */
		this.vt = 0;

		this.iter = 0;

		int bodiesPerCore = nBodies / cores;
		for(int i = 0; i < cores -1; i++){
			new BodyAgent(this.readBodies.
					subList(i*bodiesPerCore,i*bodiesPerCore+bodiesPerCore-1),
						this.readBodies, this.cyclicBarrier, this.sharedList, this.context)
					.start();
		}
		new BodyAgent(this.readBodies.
				subList(bodiesPerCore*(cores -1),this.readBodies.size()-1),
				this.readBodies, this.cyclicBarrier, this.sharedList, this.context)
				.start();

	}

	private void createBodies(final int nBodies) {
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < nBodies; i++) {
			double x = context.getBoundary().getX0()*0.25 + rand.nextDouble() * (context.getBoundary().getX1() - context.getBoundary().getX0()) * 0.25;
			double y = context.getBoundary().getY0()*0.25 + rand.nextDouble() * (context.getBoundary().getY1() - context.getBoundary().getY0()) * 0.25;
			Body b = new Body(i, new P2d(x, y), new V2d(0, 0), 10);
			readBodies.add(new Body(b));
		}
		sharedList.addBodies(readBodies);
	}

}
