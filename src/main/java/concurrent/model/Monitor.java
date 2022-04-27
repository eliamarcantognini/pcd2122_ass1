package concurrent.model;

public class Monitor {

    private final long bodyToWait;
    private long counter = 0;

    public Monitor(long bodyToWait) {
        this.bodyToWait = bodyToWait;
    }

    public synchronized void countMeIn(){
        counter++;
        notifyAll();
    }

    public synchronized void awaitCompletion() throws InterruptedException {
        while (counter < bodyToWait) {
            wait();
        }
    }

    public synchronized void reset() {
        counter = 0;
    }
}
