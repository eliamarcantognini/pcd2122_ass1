package concurrent.model;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BodyAgent extends Thread{
    private final Body body;
    private final CyclicBarrier cyclicBarrier;

    public BodyAgent(final Body body, final CyclicBarrier cyclicBarrier){
        this.body = body;
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        System.out.println("Thread of: " + this.body.getId() + " running...");
        try {
            this.cyclicBarrier.await();
        } catch (Exception e) {
            System.out.println("Aiha?");
            System.exit(-1);
        }
        System.out.println("Thread of: " + this.body.getId() + " after await...");
    }
}
