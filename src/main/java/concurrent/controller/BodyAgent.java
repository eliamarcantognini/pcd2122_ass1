package concurrent.controller;

import concurrent.model.Body;
import concurrent.model.Context;
import concurrent.model.SharedList;
import concurrent.model.V2d;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BodyAgent extends Thread{
    private final List<Body> bodiesToCompute;
    private final CyclicBarrier cyclicBarrier;
    private final List<Body> allBodies;
    private final SharedList sharedList;
    private final Context context;
    private int iteration;

    public BodyAgent(final List<Body> body, final List<Body> bodies, final CyclicBarrier cyclicBarrier, final SharedList sharedList, final Context context){
        this.bodiesToCompute = body;
        this.cyclicBarrier = cyclicBarrier;
        this.allBodies = bodies;
        this.sharedList = sharedList;
        this.context = context;
        System.out.println("Create agent - bodies: " + this.bodiesToCompute);
    }

    @Override
    public void run() {
        while (this.context.isKeepWorking()) {
            /* compute total force on bodies */

            for (Body b : this.bodiesToCompute) {
                System.out.println("Iterazione: " + this.iteration++);
                V2d totalForce = computeTotalForceOnBody(b);

                /* compute instant acceleration */
                V2d acc = new V2d(totalForce).scalarMul(1.0 / b.getMass());

                /* update velocity */
                b.updateVelocity(acc, Context.DT);

                b.updatePos(Context.DT);

                b.checkAndSolveBoundaryCollision(context.getBoundary());

                sharedList.updateBody(new Body(b));
            }

            try {
                this.cyclicBarrier.await();
            } catch (Exception e) {
                System.out.println("Thread of bodies" + this.bodiesToCompute.get(0).getId() + ": Await failed");
                System.exit(-1);
            }
        }
    }



    private V2d computeTotalForceOnBody(Body body) {

        V2d totalForce = new V2d(0, 0);

        /* compute total repulsive force */

        for (Body otherBody : this.allBodies) {
            if (!body.equals(otherBody)) {
                try {
                    V2d forceByOtherBody = body.computeRepulsiveForceBy(otherBody);
                    totalForce.sum(forceByOtherBody);
                } catch (Exception ignored) {
                }
            }
        }

        /* add friction force */
        totalForce.sum(body.getCurrentFrictionForce());

        return totalForce;
    }
}
