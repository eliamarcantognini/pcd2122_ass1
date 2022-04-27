package concurrent.model;

public class UpdateTask implements Runnable {

    private final Body body;
    private final BodiesSharedList readSharedList;
    private final BodiesSharedList writeSharedList;
    private final Context context;
    private final Monitor monitor;

    public UpdateTask(Body body, Context context, Monitor monitor) {
        this.body = new Body(body);
        this.context = context;
        this.readSharedList = this.context.getReadSharedList();
        this.writeSharedList = this.context.getWriteSharedList();
        this.monitor = monitor;
    }

    @Override
    public void run() {
        /* compute total force on bodies */
        V2d totalForce = computeTotalForceOnBody(body);
        /* compute instant acceleration */
        V2d acc = new V2d(totalForce).scalarMul(1.0 / body.getMass());
        /* update velocity */
        body.updateVelocity(acc, this.context.getDT());
        body.updatePos(this.context.getDT());
        body.checkAndSolveBoundaryCollision(context.getBoundary());
        writeSharedList.updateBody(body);
        monitor.countMeIn();
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
