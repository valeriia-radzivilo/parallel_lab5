package task1;

import shared.QueueProcessingSimulation;
import shared.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    static final int SIMULATION_COUNT = 5;
    static final int QUEUE_SIZE = 100;

    static final boolean SHOW_CRITICAL_VALUES = true;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Callable<Result>> simulations = new ArrayList<>();
        for (int i = 0; i < SIMULATION_COUNT; i++) {
            simulations.add(new QueueProcessingSimulation(QUEUE_SIZE, SHOW_CRITICAL_VALUES));
        }
        List<Future<Result>> results = executor.invokeAll(simulations);
        executor.shutdown();
        
        double avrageQueueSize = 0;
        double failuresCount = 0;


        for (Future<Result> taskResult : results) {
            Result result = taskResult.get();
            avrageQueueSize += result.averageQueueSize();
            failuresCount += result.rejectedPercentage();
        }

        System.out.println("\n\nParallel final results:");
        System.out.println(failuresCount / results.size());
        System.out.println(avrageQueueSize / results.size());
    }
}