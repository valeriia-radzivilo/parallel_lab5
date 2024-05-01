package shared;

import java.util.Random;

public class Channel extends Thread {

    private final static int MAX_TIME = 50_000;
    private final ServiceQueue serviceQueue;

    Channel(ServiceQueue queue) {
        this.serviceQueue = queue;
    }

    @Override
    public void run() {
        Random random = new Random();
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;

        while (elapsedTime < MAX_TIME) {
            this.serviceQueue.addItem(random.nextInt(1234));

            try {
                Thread.sleep(random.nextInt(15)); // Imitating data processing
            } catch (InterruptedException ignored) {
            }

            elapsedTime = System.currentTimeMillis() - startTime;
        }

        this.serviceQueue.finishQueue();
    }
}