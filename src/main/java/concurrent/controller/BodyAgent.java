package concurrent.controller;

import concurrent.model.BodiesSharedList;
import concurrent.model.Body;
import concurrent.model.Context;
import concurrent.model.V2d;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BodyAgent extends Thread {
    private final int startIndex;
    private final int endIndex;
    private final CyclicBarrier cyclicBarrier;
    private final List<Body> allBodies;
    private final BodiesSharedList sharedList;
    private final Context context;

    public BodyAgent(int startIndex, int endIndex, final List<Body> bodies, final CyclicBarrier cyclicBarrier, final BodiesSharedList sharedList, final Context context) {
        super.setName("BodyAgent" + startIndex);
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.cyclicBarrier = cyclicBarrier;
        this.allBodies = bodies;
        this.sharedList = sharedList;
        this.context = context;
    }

    @Override
    public void run() {
        while (this.context.isKeepWorking()) {

            List<Body> bodiesToCompute = allBodies.subList(this.startIndex, this.endIndex);

            for (Body b : bodiesToCompute) {
                Body body = new Body(b);
                /* compute total force on bodies */
                V2d totalForce = computeTotalForceOnBody(body);
                /* compute instant acceleration */
                V2d acc = new V2d(totalForce).scalarMul(1.0 / body.getMass());
                /* update velocity */
                body.updateVelocity(acc, Context.DT);
                body.updatePos(Context.DT);
                body.checkAndSolveBoundaryCollision(context.getBoundary());
                sharedList.updateBody(body);
            }

            try {
                this.cyclicBarrier.await();
            } catch (Exception e) {
                System.out.println("Thread of bodies" + bodiesToCompute.get(0).getId() + ": Await failed");
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
