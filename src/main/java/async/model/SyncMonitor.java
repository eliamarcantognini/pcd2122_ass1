package async.model;

public class SyncMonitor {

    private boolean running = false;

    public synchronized void startSimulation() {
        running = true;
    }

    public synchronized void stopSimulation() {
        running = false;
    }

    public synchronized boolean shouldContinue() {
        return running;
    }
}
