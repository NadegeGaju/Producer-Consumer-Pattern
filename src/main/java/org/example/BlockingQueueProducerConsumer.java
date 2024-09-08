package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueProducerConsumer {
    private static final int CAPACITY = 10;
    private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(CAPACITY);

    // Producer method
    public void produce(int item) throws InterruptedException {
        queue.put(item); // Automatically blocks if the queue is full
        System.out.println("Produced: " + item);
    }

    // Consumer method
    public void consume() throws InterruptedException {
        int item = queue.take(); // Automatically blocks if the queue is empty
        System.out.println("Consumed: " + item);
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueueProducerConsumer pc = new BlockingQueueProducerConsumer();

        Runnable producerTask = () -> {
            try {
                for (int i = 0; i < 20; i++) {
                    pc.produce(i);
                    Thread.sleep(50); // Simulate production time
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable consumerTask = () -> {
            try {
                for (int i = 0; i < 20; i++) {
                    pc.consume();
                    Thread.sleep(60); // Simulate consumption time
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // Measure performance
        long startTime = System.currentTimeMillis();

        // Create multiple producers and consumers
        Thread producer1 = new Thread(producerTask);
        Thread producer2 = new Thread(producerTask);
        Thread consumer1 = new Thread(consumerTask);
        Thread consumer2 = new Thread(consumerTask);

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();

        producer1.join();
        producer2.join();
        consumer1.join();
        consumer2.join();

        long endTime = System.currentTimeMillis();
        System.out.println("BlockingQueue Producer-Consumer Time: " + (endTime - startTime) + "ms");
    }
}