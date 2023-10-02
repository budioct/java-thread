package com.tutorial.modern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ThreadLocalRandomTest {

    /**
     * ● Saat menggunakan object Random secara parallel, maka di dalam class Random kita akan
     *   melakukan sharing variable, hal ini membuat class Random tidak aman dan juga lambat
     * ● Oleh karena itu terdapat class ThreadLocalRandom, ThreadLocalRandom merupakan class yang
     *   seperti ThreadLocal, namun spesial untuk Random, sehingga kita bisa membuat angka random
     *   tanpa khawatir dengan race condition, karena object Random nya akan berbeda tiap thread
     */

    @Test
    void testCreateThreadLocalRandom() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(8);

        for (int i = 0; i < 50; i++) {
            final int task = i;
            executorService.execute(() -> {
                try {
                    Thread.sleep(2000L);
                    int valueRandom = ThreadLocalRandom.current().nextInt();
                    System.out.println("Task-" + task + " ,saya menaruh data di sini dengan code:" + UUID.randomUUID().toString() + "... valueRandom: " + valueRandom);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

    }

    /**
     * ThreadLocalRandom Stream
     * method di ThreadLocalRandom seperti ints(), longs() dan doubles() yang mengembalikan data stream
     */

    @Test
    void testCreateThreadLocalRandomStream() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(8);

        executorService.execute(() -> {
            ThreadLocalRandom.current().ints(1000, 0, 1000)
                    .forEach((value) -> System.out.println("Value Random Stream: " + value));
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);


    }

}
