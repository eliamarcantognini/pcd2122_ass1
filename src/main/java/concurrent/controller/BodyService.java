package concurrent.controller;

import concurrent.model.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BodyService extends Thread {

    final private ExecutorService executor;

    final private long nSteps;
    private boolean stopFromGUI;
    private BodiesSharedList readSharedList;
    private BodiesSharedList writeSharedList;
    private Context context;
    private Simulator simulator;
    private Monitor monitor;

    public BodyService(long nSteps, Context context, Simulator simulator) {
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);
        this.nSteps = nSteps;
        this.context = context;
        this.readSharedList = context.getReadSharedList();
        this.writeSharedList = context.getWriteSharedList();
        this.simulator = simulator;
        this.monitor = new Monitor(readSharedList.getBodies().size());
        this.stopFromGUI = false;
    }

    //TODO Contrallare i parametri passati

    @Override
    public void run() {
        for (int iter = 0; iter < nSteps && !stopFromGUI; iter++){
            for (Body b: readSharedList.getBodies()) {
                executor.execute(new UpdateTask(b, this.context, this.monitor));
            }
            try {
                monitor.awaitCompletion();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            monitor.reset();
            simulator.updateSimulation();
        }
        simulator.initSimulation();
    }
}
