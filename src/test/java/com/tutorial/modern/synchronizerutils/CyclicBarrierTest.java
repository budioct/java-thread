package com.tutorial.modern.synchronizerutils;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class CyclicBarrierTest {

    /**
     * Note: CyclicBarrier merupakan fitur yang bisa kita gunakan untuk saling menunggu, sampai jumlah thread yang menunggu terpenuhi
     * Diawal kita akan tentukan berapa jumlah thread yang menunggu, jika sudah terpenuhi, maka secara otomatis proses menunggu akan selesai
     */

    @Test
    void testCreateCyclicBarrier() throws InterruptedException {

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
        final ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            final int task = i;
            executorService.execute(() -> {
                try {
                    cyclicBarrier.await(); // int await() // Tunggu sampai semua pihak telah meminta awaitpenghalang ini.
                    System.out.println("Task: " + task + " ,Done Waiting");
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

    }
}
