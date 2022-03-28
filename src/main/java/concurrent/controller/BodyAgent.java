package concurrent.controller;

import concurrent.model.Body;
import concurrent.model.SharedList;
import concurrent.model.V2d;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class BodyAgent extends Thread{
    private final Body body;
    private final CyclicBarrier cyclicBarrier;
    private final List<Body> bodies;
    private final SharedList sharedList;


    public BodyAgent(final Body body, final List<Body> bodies, final CyclicBarrier cyclicBarrier, final SharedList sharedList){
        this.body = body;
        this.cyclicBarrier = cyclicBarrier;
        this.bodies = bodies;
        this.sharedList = sharedList;
    }

    @Override
    public void run() {
        while (Simulator.keepWorking) {
            /* compute total force on bodies */
            V2d totalForce = computeTotalForceOnBody();

            /* compute instant acceleration */
            V2d acc = new V2d(totalForce).scalarMul(1.0 / this.body.getMass());

            /* update velocity */
            this.body.updateVelocity(acc, Simulator.DT);

            this.body.updatePos(Simulator.DT);

            this.body.checkAndSolveBoundaryCollision(Simulator.BOUNDS);

            sharedList.updateBody(new Body(this.body));

            try {
                this.cyclicBarrier.await();
            } catch (Exception e) {
                System.out.println("Aiha?");
                System.exit(-1);
            }
        }
    }

    private V2d computeTotalForceOnBody() {

        V2d totalForce = new V2d(0, 0);

        /* compute total repulsive force */

        for (Body otherBody : this.bodies) {
            if (!this.body.equals(otherBody)) {
                try {
                    V2d forceByOtherBody = this.body.computeRepulsiveForceBy(otherBody);
                    totalForce.sum(forceByOtherBody);
                } catch (Exception ignored) {
                }
            }
        }

        /* add friction force */
        totalForce.sum(this.body.getCurrentFrictionForce());

        return totalForce;
    }
}
