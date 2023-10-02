package com.tutorial.modern.synchronizerutils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SemaphoreTest {

    /**
     * Note: Semaphore merupakan class yang digunakan untuk manage data counter
     * Semaphore cocok sekali misal untuk menjaga agar thread berjalan pada maksimal total counter yang sudah kita tentukan
     */

    @Test
    void testCreateSemaphore() throws InterruptedException {

        final Semaphore semaphore = new Semaphore(1); // manage thread yang di izinkan
        final ExecutorService executorService = Executors.newFixedThreadPool(4); // set thread execution

        for (int i = 0; i < 1000; i++) {
            final int task = i;
            executorService.execute(() -> {
                try {
                    semaphore.acquire(); // void acquire() // Memperoleh izin dari semafor ini, memblokir hingga tersedia, atau utas terputus .
                    Thread.sleep(500L);
                    System.out.println("task: " + task + " Thread: " + Thread.currentThread().getName() + " ,Finish");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    semaphore.release(); // void release() // Melepaskan izin, mengembalikannya ke semaphore.
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

    }

}
