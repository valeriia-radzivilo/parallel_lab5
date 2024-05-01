package shared;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ServiceQueue {
    private final int queueSize;
    private final Queue<Integer> queue;
    private final List<Integer> queuesSizes;
    private boolean isOver;
    private int consumedCount;
    private int countRejected;

    public ServiceQueue(int queueSize) {
        this.consumedCount = 0;
        this.countRejected = 0;
        this.isOver = false;
        this.queue = new ArrayDeque<>();
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
        this.isOver = true;
    }

    boolean isNotOver() {
        return !this.isOver;
    }

    public synchronized int length() {
        return this.queue.size();
    }

    public synchronized void addItem(int item) {
        if (this.queue.size() >= this.queueSize) {
            this.countRejected += 1;
            return;
        }

        this.queue.add(item);
        notifyAll();
    }

    public synchronized void getItem() {
        this.queuesSizes.add(this.queue.size());
        while (this.queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        this.queue.poll();
    }

    public double rejectedPossibility() {
        return (double) this.countRejected / (this.countRejected + this.consumedCount);
    }

    public double averageQueueSize() {
        int sum = 0;
        for (int i : queuesSizes) {
            sum += i;
        }
        return (double) sum / queuesSizes.size();
    }

}