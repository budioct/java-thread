package com.tutorial.modern.forkjoin;

import com.tutorial.data.SimpleForkJoinTaskRecursiveAction;
import com.tutorial.data.SimpleForkJoinTaskRecursiveTask;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootTest
public class ForkJoinTest {

    @Test
    void testCreateForkJoin() throws InterruptedException {

        ForkJoinPool forkJoinPool1 = ForkJoinPool.commonPool(); // static ForkJoinPool commonPool() // total cpu core parallelism // mengikuti core thread cpu kita
        ForkJoinPool forkJoinPool2 = new ForkJoinPool(4);

        ExecutorService executorService1 = Executors.newWorkStealingPool(); // static ExecutorService newWorkStealingPool() // total cpu core parallelism // mengikuti core thread cpu kita
        ExecutorService executorService2 = Executors.newWorkStealingPool(4);

        for (int i = 0; i < 1000; i++) {
            final int task = i;
            executorService2.execute(() -> {
                try {
                    Thread.sleep(2000L);
//                    System.out.println("Task-" + task + " ,saya menaruh data di sini dengan code:" + UUID.randomUUID().toString());
                    System.out.println("Task-" + task + " ,Thread: " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService2.shutdown();
        executorService2.awaitTermination(1, TimeUnit.DAYS);

        /**
         * result:
         * instance ForkJoinPool.commonPool()
         * Task-7 ,Thread: main
         * Task-0 ,Thread: ForkJoinPool.commonPool-worker-1
         * Task-1 ,Thread: ForkJoinPool.commonPool-worker-2
         * Task-6 ,Thread: ForkJoinPool.commonPool-worker-7
         * Task-2 ,Thread: ForkJoinPool.commonPool-worker-3
         * Task-5 ,Thread: ForkJoinPool.commonPool-worker-6
         * Task-4 ,Thread: ForkJoinPool.commonPool-worker-5
         * Task-3 ,Thread: ForkJoinPool.commonPool-worker-4
         *
         * instance
         * Executors.newWorkStealingPool()
         * Task-6 ,Thread: ForkJoinPool-2-worker-8
         * Task-5 ,Thread: ForkJoinPool-2-worker-6
         * Task-4 ,Thread: ForkJoinPool-2-worker-5
         * Task-2 ,Thread: ForkJoinPool-2-worker-3
         * Task-3 ,Thread: ForkJoinPool-2-worker-4
         * Task-7 ,Thread: ForkJoinPool-2-worker-7
         * Task-0 ,Thread: ForkJoinPool-2-worker-1
         * Task-1 ,Thread: ForkJoinPool-2-worker-2
         */

    }

    @Test
    void testForkJoinTaskRecursiveAction() throws InterruptedException {

        // RecursiveAction eksekusi Runnable task yang di eksekusi tidak ada return value

//        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        ForkJoinPool forkJoinPool = new ForkJoinPool(2);
        List<Integer> integerList = IntStream.range(0, 1000).boxed()
                .collect(Collectors.toList()); // loop dari range 0 sampai 1000

        forkJoinPool.execute(new SimpleForkJoinTaskRecursiveAction(integerList)); // SimpleForkJoinTask extends RecursiveAction

        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.DAYS);

        /**
         * result:
         * ForkJoinPool-1-worker-1 : [0, 1, 2, 3, 4, 5, 6]
         * ForkJoinPool-1-worker-1 : [0, 1, 2, 3, 4, 5, 6]
         * ForkJoinPool-1-worker-2 : [500, 501, 502, 503, 504, 505, 506]
         * ForkJoinPool-1-worker-1 : [0, 1, 2, 3, 4, 5, 6]
         * ForkJoinPool-1-worker-1 : [0, 1, 2, 3, 4, 5, 6]
         */

    }

    @Test
    void testForkJoinTaskRecursiveTask() throws InterruptedException {

        // RecursiveTask eksekusi Callable/Runnable task yang di eksekusi ada return value

//        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        ForkJoinPool forkJoinPool = new ForkJoinPool(2);
        List<Integer> integerList = IntStream.range(0, 1000).boxed()
                .collect(Collectors.toList()); // loop dari range 0 sampai 1000

        SimpleForkJoinTaskRecursiveTask task = new SimpleForkJoinTaskRecursiveTask(integerList);
        forkJoinPool.submit(task);

        Long totalValue = task.join();
        System.out.println("Total Execute: " + totalValue);

        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.DAYS);

        /**
         * result:
         * ForkJoinPool-1-worker-2 : [429, 430, 431, 432, 433, 434, 435, 436]
         * ForkJoinPool-1-worker-2 : [429, 430, 431, 432, 433, 434, 435, 436]
         * ForkJoinPool-1-worker-2 : [429, 430, 431, 432, 433, 434, 435, 436]
         * ForkJoinPool-1-worker-2 : [429, 430, 431, 432, 433, 434, 435, 436]
         * Total Execute: 499500
         */

    }

    @Test
    void CreateForkJoinPoolParallelStream(){

        Stream<Integer> stream = IntStream.range(0, 1000).boxed();
        stream.parallel().forEach(task -> {
            System.out.println("Task-" + task + " ,Thread: " + Thread.currentThread().getName());
        }); // method parallel() // Secara default, Parallel Stream akan dijalankan di ForkJoinPool

        /**
         * result:
         * Task-662 ,Thread: main
         * Task-663 ,Thread: main
         * Task-664 ,Thread: main
         * Task-312 ,Thread: ForkJoinPool.commonPool-worker-1
         * Task-313 ,Thread: ForkJoinPool.commonPool-worker-1
         * Task-314 ,Thread: ForkJoinPool.commonPool-worker-1
         * Task-315 ,Thread: ForkJoinPool.commonPool-worker-1
         */

    }

    @Test
    void testCreateCustomForkJoinPool(){

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        ForkJoinTask<?> result = forkJoinPool.submit(() -> {
            Stream<Integer> stream = IntStream.range(0, 1000).boxed();
            stream.parallel().forEach(task -> {
                System.out.println("Task-" + task + " ,Thread: " + Thread.currentThread().getName());
            });
        }); // parallel() di Java Stream tidak memiliki parameter // lantas mengeksekusi parallel stream nya dalam task nya ForkJoinPool

        result.join();

        /**
         * result:
         * Task-656 ,Thread: ForkJoinPool-1-worker-1
         * Task-657 ,Thread: ForkJoinPool-1-worker-1
         * Task-658 ,Thread: ForkJoinPool-1-worker-1
         * Task-659 ,Thread: ForkJoinPool-1-worker-1
         * Task-660 ,Thread: ForkJoinPool-1-worker-1
         */
    }


}
