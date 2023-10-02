package com.tutorial.modern.synchronizerutils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ExchangerTest {

    /**
     * Note:
     * Exchanger merupakan fitur synchronizer untuk melakukan pertukaran data antar thread
     * Jika data belum tersedia, maka thread yang melakukan pertukaran akan menunggu sampai ada thread lain yang melakukan pertukaran data
     */

    @Test
    void testCreateExchanger() throws InterruptedException {

        Exchanger<String> exchanger = new Exchanger<>();
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // thread 1.. hasil print out result set dari thread 2
        executorService.execute(() -> {
            try {
                Thread.sleep(1000L);
                String first = exchanger.exchange("First"); // V exchange(V x) // Menunggu thread lain tiba di titik pertukaran ini (kecuali thread saat ini terputus ), dan kemudian mentransfer objek tertentu ke sana, menerima objeknya sebagai imbalan.
                System.out.println("1. " + first + " with thread: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // thread 2.. hasil print out result set dari thread 1
        executorService.execute(() -> {
            try {
                Thread.sleep(2000L);
                String second = exchanger.exchange("Second"); // V exchange(V x) // Menunggu thread lain tiba di titik pertukaran ini (kecuali thread saat ini terputus ), dan kemudian mentransfer objek tertentu ke sana, menerima objeknya sebagai imbalan.
                System.out.println("2. " + second + " with thread: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

    }

}
