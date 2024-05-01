package shared;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class QueueProcessingSimulation implements Callable<Result> {

    private final ServiceQueue queue;
    private final boolean showCriticalValues;

    public QueueProcessingSimulation(int queueSize, boolean showCriticalValues) {
        queue = new ServiceQueue(queueSize);
        this.showCriticalValues = showCriticalValues;
    }

    public Result call() {
        Channel channel = new Channel(queue);
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < 5; i++) {
            forkJoinPool.execute(new Customer(queue));
        }


        QueueMonitor queueMonitor = new QueueMonitor(queue, showCriticalValues);
        forkJoinPool.execute(queueMonitor);

        forkJoinPool.execute(channel);
        forkJoinPool.shutdown();
        try {
            forkJoinPool.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }

        return new Result(queue.rejectedPossibility(), queue.averageQueueSize());
    }
}