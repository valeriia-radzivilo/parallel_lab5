import shared.QueueProcessingSimulation;
import shared.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class Main {

    static final int SIMULATION_COUNT = 5;
    static final int QUEUE_SIZE = 1000;

    static final boolean SHOW_CRITICAL_VALUES = true;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        List<Callable<Result>> simulations = new ArrayList<>();
        for (int i = 0; i < SIMULATION_COUNT; i++) {
            simulations.add(new QueueProcessingSimulation(QUEUE_SIZE, SHOW_CRITICAL_VALUES));
        }
        List<Future<Result>> results = forkJoinPool.invokeAll(simulations);

        double avrageQueueSize = 0;
        double rejectionsCount = 0;

        for (Future<Result> taskResult : results) {
            Result result = taskResult.get();
            avrageQueueSize += result.averageQueueSize();
            rejectionsCount += result.rejectedPercentage();
        }

        System.out.println("\n\nParallel final results:");
        System.out.println("Rejections rate : " + rejectionsCount / results.size());
        System.out.println("Average queue size : " + avrageQueueSize / results.size());

        forkJoinPool.shutdown();
    }
}