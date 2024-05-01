package shared;


public class QueueMonitor extends Thread {
    private final ServiceQueue queue;
    private final boolean showCriticalValues;

    public QueueMonitor(ServiceQueue queue, boolean showCriticalValues) {
        this.queue = queue;
        this.showCriticalValues = showCriticalValues;
    }

    @Override
    public void run() {
        while (queue.isInProcess()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Handle exception
            }
            if (showCriticalValues)
                System.out.println("VALUES: queue length: " + queue.length() + " - rejected possibility: " + queue.rejectedPossibility());
        }
    }
}