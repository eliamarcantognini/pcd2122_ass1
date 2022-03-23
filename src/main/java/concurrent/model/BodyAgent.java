package concurrent.model;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BodyAgent extends Thread{
    private final Body body;
    private final CyclicBarrier cyclicBarrier;
    private final List<Body> bodies;


    public BodyAgent(final Body body, final List<Body> bodies, final CyclicBarrier cyclicBarrier){
        this.body = body;
        this.cyclicBarrier = cyclicBarrier;
        this.bodies = bodies;
    }

    @Override
    public void run() {
        for (int i = 0; i<2; i++) {
            System.out.println("Thread of: " + this.body.getId() + " BBBBB wait...");
            try {
                this.cyclicBarrier.await();
            } catch (Exception e) {
                System.out.println("Aiha?");
                System.exit(-1);
            }

            //System.out.println(this.bodies.toString());
            System.out.println("Thread of: " + this.body.getId() + " AA wait...");
        }
    }
}
