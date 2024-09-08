package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class BasicProducerConsumer {
    private static final int MAX_SIZE = 10;
    private final Queue<Integer> buffer = new LinkedList<>();

    // Producer method
    public synchronized void produce(int item) {
        try {
            while (buffer.size() == MAX_SIZE) {
                wait(); // Wait until there's space in the buffer
            }
            buffer.add(item);
            System.out.println("Produced: " + item);
            notifyAll(); // Notify consumers that there's a new item
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Re-interrupt the thread
            System.err.println("Producer thread interrupted: " + e.getMessage());
        }
    }

    // Consumer method
    public synchronized int consume() {
        try {
            while (buffer.isEmpty()) {
                wait(); // Wait until there's something to consume
            }
            int item = buffer.poll();
            System.out.println("Consumed: " + item);
            notifyAll(); // Notify producers that there's space in the buffer
            return item;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Re-interrupt the thread
            System.err.println("Consumer thread interrupted: " + e.getMessage());
            return -1;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BasicProducerConsumer pc = new BasicProducerConsumer();

        Runnable producerTask = () -> {
            for (int i = 0; i < 20; i++) {
                pc.produce(i);
                try {
                    Thread.sleep(50); // Simulate production time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Runnable consumerTask = () -> {
            for (int i = 0; i < 20; i++) {
                pc.consume();
                try {
                    Thread.sleep(60); // Simulate consumption time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
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
        System.out.println("Basic Producer-Consumer Time: " + (endTime - startTime) + "ms");
    }
}
