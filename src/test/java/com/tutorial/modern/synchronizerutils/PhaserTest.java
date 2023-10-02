package com.tutorial.modern.synchronizerutils;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class PhaserTest {

    /**
     * Note:
     * Phaser merupakan fitur synchronizer yang mirip dengan CyclicBarrier dan CountDownLatch, namun lebih flexible
     * register() atau bulkRegister(int)    untuk merubah thread yang sudah di tentukan
     * arrive...() atau await...(int)       untuk menurunkan thread yang sudah di tentukan
     */

    @Test
    void testCreatePharserAsCountDownLatch() throws InterruptedException {

        final var phaser = new Phaser();
        final var service = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            final int task = i;
            service.execute(() -> {
                try {
                    phaser.register(); // int register() // Menambahkan thread yang sudah di tentukan
                    System.out.println("Start Task-" + task);
                    Thread.sleep(2000L);
                    System.out.println("End Task-" + task);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    phaser.arrive(); // int arrive() // Menurunkan thread yang sudah di tentukan
                }
            });
        }

        service.execute(() -> {
            phaser.awaitAdvance(0); // int awaitAdvance(int phase) // Menunggu fase dari fase ini untuk maju dari nilai fase yang diberikan, segera kembali jika fase saat ini tidak sama dengan nilai fase yang ditentukan atau fase ini dihentikan.
            System.out.println("All Task Finished");
        });

        service.shutdown();
        service.awaitTermination(1, TimeUnit.DAYS);

    }

    @Test
    void testCreatePharserAsCyclicBarrier() throws InterruptedException {

        final var phaser = new Phaser();
        final var service = Executors.newFixedThreadPool(10);

        phaser.bulkRegister(5); // int bulkRegister(int parties) // Menambahkan jumlah pihak baru yang belum tiba ke fase ini.
        for (int i = 0; i < 5; i++) {
            final int task = i;
            service.execute(() -> {
                phaser.arriveAndAwaitAdvance();
                System.out.println("Done Waiting, Task-" + task);
            });
        }

        service.shutdown();
        service.awaitTermination(1, TimeUnit.DAYS);

    }

}
