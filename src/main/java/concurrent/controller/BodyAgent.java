package concurrent.controller;

import concurrent.model.Body;
import concurrent.model.P2d;
import concurrent.model.SyncList;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BodyAgent extends Thread{
    private final Body body;
    private final CyclicBarrier cyclicBarrier;
    private final List<Body> bodies;
    private final SyncList monitorList;


    public BodyAgent(final Body body, final List<Body> bodies, final CyclicBarrier cyclicBarrier, final SyncList monitorList){
        this.body = body;
        this.cyclicBarrier = cyclicBarrier;
        this.bodies = bodies;
        this.monitorList = monitorList;
    }

    @Override
    public void run() {
        while (true){
//            System.out.println("Thread of: " + this.body.getId() + " this is my list: " + this.bodies);
//            System.out.println("Thread of: " + this.body.getId() + " updating the monitor list");
            monitorList.updateBody(new Body(body.getId(), this.body.getPos().sum(this.body.getVel()), this.body.getVel(), this.body.getMass()));
//            System.out.println("Thread of: " + this.body.getId() + " updated the monitor list");
            try {
//                System.out.println("Thread of: " + this.body.getId() + "I'm crushing into the barrier");
                this.cyclicBarrier.await();
            } catch (Exception e) {
                System.out.println("Aiha?");
                System.exit(-1);
            }
            //System.out.println(this.bodies.toString());
//            System.out.println("Thread of: " + this.body.getId() + " checking list IF it's updated");
//            System.out.println("Thread of: " + this.body.getId() + " this is my NEW(?) list: " + this.bodies);
        }
    }
}
