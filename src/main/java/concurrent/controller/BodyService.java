package concurrent.controller;

import concurrent.model.BodiesSharedList;
import concurrent.model.Body;
import concurrent.model.Context;
import concurrent.model.UpdateTask;

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

    public BodyService(long nSteps, Context context, Simulator simulator) {
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);
        this.nSteps = nSteps;
        this.context = context;
        this.readSharedList = context.getReadSharedList();
        this.writeSharedList = context.getWriteSharedList();
        this.simulator = simulator;
        this.stopFromGUI = false;
    }

    //TODO Contrallare i parametri passati

    @Override
    public void run() {
        System.out.println("Sono Service, vado!");
        for (int iter = 0; iter <= nSteps || !stopFromGUI; iter++){
            for (Body b: readSharedList.getBodies()) {
                System.out.println("faccio un body");
                executor.execute(new UpdateTask(b, this.context));
            }
            //monitor.wait(); Aspettare la fine di tutti i task tramite un monitor
            simulator.updateSimulation();
        }
        simulator.initSimulation();
    }
}
