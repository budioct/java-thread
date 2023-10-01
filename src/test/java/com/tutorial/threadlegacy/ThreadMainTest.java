package com.tutorial.threadlegacy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class ThreadMainTest {

    /**
     * // Thread adalah object yang digunakan untuk menjalankan Runnable secara Asychronouse (eksekusi task berbarengan)
     * // Thread adalah proses ringan
     *
     * secara default akan selalu menunggu semua user thread selesai sebelum program berhenti
     */

    @Test
    void testMainThread() {
        String name = Thread.currentThread().getName(); // Thread Utama jadi Object Thread // result: main
        log.info("Name Thread: {}", name); // main
    }

    @Test
    void testCreateThreadWithWork() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello from Thread " + Thread.currentThread().getName());
                System.out.println("Worker " + Thread.currentThread().getState());
                System.out.println(Thread.currentThread().getContextClassLoader().getName());
                System.out.println(Thread.currentThread().getThreadGroup());
            }
        };

        Thread thread = new Thread(runnable);
        thread.start(); // void start() // running thread

    }

    @Test
    void testThreadSleep() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2_000); // void sleep(long millis) // thread menjadi tertidur
                    System.out.println("Hello from Thread " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();
        Thread.sleep(3_000); // menunggu runnable yang di set tertidur

    }

    @Test
    void testThreadJoin() throws InterruptedException {

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000); // void sleep(long millis) // thread menjadi tertidur
                System.out.println("Hello From Thread: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start(); // void start() // eksekusi task thread
        thread.join(); // void join() // menunggu hingga task thread selesai
    }

    @Test
    void testThreadInterrupt() throws InterruptedException {
        var thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("Task Runnable: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return; // saat memangil interrupt(), pengecekan  pengecekan interrupted() akan bernilai true
                }
            }
        });

        thread.start(); // void start() // eksekusi task thread
        Thread.sleep(5000); // menunggu runnable yang di set tertidur
        thread.interrupt(); // void interrupt() // mengirim sinyal ke thread bahwa harus berhenti eksekusi task thread
        thread.join(); // void join() // menunggu hingga task thread selesai
    }

    @Test
    void testSetNameThread() throws InterruptedException {
        var thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("Hello From Thread: " + Thread.currentThread().getName() + " " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return; // saat memangil interrupt(), pengecekan  pengecekan interrupted() akan bernilai true
                }
            }
        });

        thread.setName("task"); // setName(param) nama thread
        thread.start(); // void start() // eksekusi task thread
        Thread.sleep(5000); // menunggu runnable yang di set tertidur
        thread.interrupt(); // void interrupt() // mengirim sinyal ke thread bahwa harus berhenti eksekusi task thread
        thread.join(); // void join() // menunggu hingga task thread selesai
    }

    @Test
    void testThreadGetState() throws InterruptedException {

        Thread thread = new Thread(() -> {
            System.out.println("worker .getState(): " + Thread.currentThread().getState());
            System.out.println("Hello from thread: " + Thread.currentThread().getName());
        });

        System.out.println("Before .start() " + thread.getState());
        thread.start();
        thread.join();
        System.out.println("After .join() " + thread.getState());

    }

    @Test
    void testThreadDaemon() throws InterruptedException {

        // Daemon thread tidak akan ditunggu jika memang program Java akan berhenti

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("Hello From Thread Deamon");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.setDaemon(false); // akan merubah cara kerja thead default menjadi daemon
        thread.start();

    }


}
