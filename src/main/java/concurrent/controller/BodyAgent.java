package concurrent.controller;

import concurrent.model.BodiesSharedList;
import concurrent.model.Body;
import concurrent.model.Context;
import concurrent.model.V2d;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Class that represents the Agents working on the system. Extends thread because he has to execute on a personal
 * control flow. Its main responsibility is to compute the forces acting on a group of bodies and update their
 * position every cycle of the simulation. The bodies that the Agent has to manage are identified by their
 * index in the shared list.
 */
public class BodyAgent extends Thread {
    private final int startIndex;
    private final int endIndex;
    private final CyclicBarrier cyclicBarrier;
    private final BodiesSharedList readSharedList;
    private final BodiesSharedList writeSharedList;
    private final Context context;

    /**
     * Create the Agent, setting the name of thread for debug purpose and initialing the variables needed for its
     * computation. The indexes are used to indicate the bodies the Agent has to manage, which are contained in the
     * shared list of the {@link Context}
     *
     * @param startIndex - index of the first body that the Agent has to manage
     * @param endIndex - index of the last body that the Agent has to manage
     * @param cyclicBarrier - the barrier where the Agents wait for the other to finish their computation
     * @param context - context of the system used to recover the shared list of bodies
     */
    public BodyAgent(int startIndex, int endIndex, final CyclicBarrier cyclicBarrier, final Context context) {
        super.setName("BodyAgent" + startIndex);
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.cyclicBarrier = cyclicBarrier;
        this.readSharedList = context.getReadSharedList();
        this.writeSharedList = context.getWriteSharedList();
        this.context = context;
    }

    @Override
    public void run() {
        while (this.context.isKeepWorking()) {

            List<Body> bodiesToCompute = readSharedList.getBodies().subList(this.startIndex, this.endIndex);

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
                writeSharedList.updateBody(body);
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
        for (Body otherBody : this.readSharedList.getBodies()) {
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
