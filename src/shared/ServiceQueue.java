package shared;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ServiceQueue {
    private final int queueSize;
    private final Queue<Integer> dataQueue;
    private final List<Integer> queuesSizes;


    private boolean isFinished;
    private int consumedCount;
    private int rejectedCount;

    public ServiceQueue(int queueSize) {
        this.consumedCount = 0;
        this.rejectedCount = 0;
        this.isFinished = false;
        this.dataQueue = new ArrayDeque<>();
        this.queueSize = queueSize;
        queuesSizes = new ArrayList<>();
    }

    int getConsumedCount() {
        return this.consumedCount;
    }

    void setConsumedCount(int consumedCount) {
        this.consumedCount = consumedCount;
    }

    void finishQueue() {
        this.isFinished = true;
    }

    boolean isInProcess() {
        return !this.isFinished;
    }

    public synchronized int length() {
        return this.dataQueue.size();
    }

    public synchronized void addItem(int item) {
        if (this.dataQueue.size() >= this.queueSize) {
            this.rejectedCount += 1;
            return;
        }

        this.dataQueue.add(item);
        notifyAll();
    }

    public synchronized int getItem() {
        this.queuesSizes.add(this.dataQueue.size());
        while (this.dataQueue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        return this.dataQueue.poll();
    }

    public double rejectedPossibility() {
        return (double) this.rejectedCount / (this.rejectedCount + this.consumedCount);
    }

    public double averageQueueSize() {
        int sum = 0;
        for (int i : queuesSizes) {
            sum += i;
        }
        return (double) sum / queuesSizes.size();
    }

}