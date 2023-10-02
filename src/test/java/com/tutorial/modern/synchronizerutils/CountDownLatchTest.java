package com.tutorial.modern.synchronizerutils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
public class CountDownLatchTest {

    /**
     * Note: CountDownLatch merupakan synchronizer yang digunakan untuk menunggu beberapa proses selesai,
     * cara kerjanya mirip dengan Semaphore, yang membedakan adalah pada CountDownLatch, counter diawal sudah ditentukan
     * <p>
     * CountDownLatch cocok jika kita misal ingin menunggu beberapa proses yang berjalan secara asynchronous sampai semua proses selesai
     */

    @Test
    void testCreateDownLatch() throws InterruptedException {

        final CountDownLatch countDownLatch = new CountDownLatch(5); // counter awal
        final ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 12; i++) {
            final int task = i;
            executorService.execute(() -> {
                try {
                    log.info("Start task");
                    Thread.sleep(2000L);
                    log.info("Task: {} ,Thread-{}", task, Thread.currentThread().getName());
                    log.info("End task");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    countDownLatch.countDown(); // void countDown() // Mengurangi jumlah dari latch, melepaskan semua thread yang menunggu jika jumlah mencapai nol.
                }
            });
        }

        executorService.execute(() -> {
            try {
                countDownLatch.await(); // void await() // Menyebabkan thread saat ini menunggu hingga kait menghitung mundur hingga nol, kecuali thread tersebut diinterupsi .
                log.info("Finish All Task");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

    }

}
