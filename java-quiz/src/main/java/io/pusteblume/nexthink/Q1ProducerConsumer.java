package io.pusteblume.nexthink;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Q1ProducerConsumer {

    private final List<Integer> queue = new LinkedList<>();

    private void processElement(Integer num) {
        System.out.println("Consumed " + num + " by " + Thread.currentThread().getName());
    }

    public class Consumer implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Integer num;
                    synchronized (queue) {
                        while (queue.isEmpty())
                            queue.wait();
                        num = queue.remove(0);
                        queue.notify();
                    }
                    processElement(num);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class Producer implements Runnable {
        final int maxSize = 1000;

        @Override
        public void run() {
            try {
                while (true)
                    synchronized (queue) {
                        while (queue.size() == maxSize)
                            queue.wait();
                        final int task = new Random().nextInt();
                        queue.add(task);
                        queue.notify();
                        System.out.println("Produced " + task);
                    }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String... args) throws InterruptedException {
        Q1ProducerConsumer q1ProducerConsumer = new Q1ProducerConsumer();
        Thread p = new Thread(q1ProducerConsumer.new Producer());
        Thread c1 = new Thread(q1ProducerConsumer.new Consumer(), "c1");
        Thread c2 = new Thread(q1ProducerConsumer.new Consumer(), "c2");

        p.start();
        c1.start();
        c2.start();

        p.join();
        c1.join();
        c2.join();
    }


}
