package shared;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < 5; i++) {
            executor.execute(new Customer(queue));
        }

        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (queue.isInProcess()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    if (showCriticalValues)
                        System.out.println("VALUES: queue length: " + queue.length() + " - rejected possibility: " + queue.rejectedPossibility());

                }
            }
        });
        executor.execute(thread);


        executor.execute(channel);
        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }

        return new Result(queue.rejectedPossibility(), queue.averageQueueSize());


    }
}