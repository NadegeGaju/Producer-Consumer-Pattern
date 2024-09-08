package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class BasicProducerConsumer {
    private static final int MAX_SIZE = 10;
    private final Queue<Integer> buffer = new LinkedList<>();

    public synchronized void produce(int item) {
        try {
            while (buffer.size() == MAX_SIZE) {
                wait();
            }
            buffer.add(item);
            System.out.println("Produced: " + item);
            notifyAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Producer thread interrupted: " + e.getMessage());
        }
    }

    public synchronized int consume() {
        try {
            while (buffer.isEmpty()) {
                wait();
            }
            int item = buffer.poll();
            System.out.println("Consumed: " + item);
            notifyAll();
            return item;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Runnable consumerTask = () -> {
            for (int i = 0; i < 20; i++) {
                pc.consume();
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        long startTime = System.currentTimeMillis();

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
