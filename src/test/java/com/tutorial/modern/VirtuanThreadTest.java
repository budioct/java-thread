package com.tutorial.modern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.IntStream;

@SpringBootTest
public class VirtuanThreadTest {

    AtomicInteger counter = new AtomicInteger();

    @Test
    void testGC() {

        System.gc();

    }

    @Test
    void platformThread() {
        while (true) {
            new Thread(() -> {
                int count = counter.incrementAndGet();
                System.out.println("Thread count = " + count);
                LockSupport.park();
            }).start();
        }

    }

    @Test
    void virtualThread() {
        while (true) {
            Thread.startVirtualThread(() -> {
                int count = counter.incrementAndGet();
                System.out.println("Thread count = " + count);
                LockSupport.park();
            });
        }
    }

    @Test
    void threadwithexecutors() {

        try (var executor = Executors.newThreadPerTaskExecutor(Executors.defaultThreadFactory())) {
            IntStream.range(0, 100_000).forEach(i -> executor.submit(() -> {
                Thread.sleep(Duration.ofSeconds(1));
                System.out.println(i);
                return i;
            }));
        }

    }

    @Test
    void threadwithvirtualthread() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 100_000).forEach(i -> executor.submit(() -> {
                Thread.sleep(Duration.ofSeconds(1));
                System.out.println(i);
                return i;
            }));
        }
    }

    @Test
    void handleOrder() throws ExecutionException, InterruptedException {
        try (var esvc = new ScheduledThreadPoolExecutor(8)) {
            // Future<Integer> inventory = esvc.submit(() -> updateInventory());
            // Future<Integer> order = esvc.submit(() -> updateOrder());

            //int theInventory = inventory.get();   // Join updateInventory
            //int theOrder = order.get();           // Join updateOrder

            //System.out.println("Inventory " + theInventory + " updated for order " + theOrder);
        }
    }

    @Test
    void testVirtualThread1() {

        // Example 1: Create Runnable. Create and start virtual thread

        Thread.ofVirtual().start(() -> {
            int task;
            for (int i = 0; i < 10; i++) {
                task = i;
                System.out.println("Task:" + task + " ,from thread: " + Thread.currentThread());
            }
        });

    }

    @Test
    void testVirtualThread2() {

        // Example 2: Create but do not start virtual thread

        Thread unstarted = Thread.ofVirtual().unstarted(() -> {
            int task;
            for (int i = 0; i < 10; i++) {
                task = i;
                System.out.println("Task:" + task + " ,from thread: " + Thread.currentThread());
            }
        });

        //unstarted.start();

        // Example 4: How to join a virtual thread
        try {
            unstarted.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    void testtestVirtualThread3() {

        List<Thread> threadList = new ArrayList<>();

        int threadCount = 100000;

        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            Thread thread = Thread.ofVirtual().start(() -> {
                int result = 1;
                for (int j = 0; j < 10; j++) {
                    result *= (j + 1);
                }
                System.out.println("Result[" + threadIndex + "]:" + result);
            });
            threadList.add(thread);
        }

        try {
            for (int i = 0; i < threadList.size(); i++) {
                threadList.get(i).join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}
