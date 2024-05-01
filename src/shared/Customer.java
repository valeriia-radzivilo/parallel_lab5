package shared;

import java.util.Random;

public class Customer extends Thread {
    private final ServiceQueue queue;

    public Customer(ServiceQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Random random = new Random();

        while (this.queue.isInProcess()) {
            this.queue.getItem();
            try {
                Thread.sleep(random.nextInt(123)); // Simulate processing time
            } catch (InterruptedException ignored) {
            }

            this.queue.setConsumedCount(this.queue.getConsumedCount() + 1);
        }
    }

}