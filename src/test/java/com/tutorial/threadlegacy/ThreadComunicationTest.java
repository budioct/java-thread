package com.tutorial.threadlegacy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ThreadComunicationTest {

    private String message = null;

    @Test
    void testThreadComunitation() throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            while (message == null) {
                // akan terus menunggu. datanya tidak akan masuk ke kondisi ini
            }
            System.out.println(message);
        });

        Thread thread2 = new Thread(() -> {
            message = "Test Thread";
        });

        thread2.start();
        thread1.start();

        thread2.join();
        thread1.join();

    }

    @Test
    void testThreadCommunicationOtomatis() throws InterruptedException {
        final Object lock = new Object();

        // wait() // menerima sinyal komunikasi antar thread
        Thread th1 = new Thread(() -> {
           synchronized (lock){
               try {
                   System.out.println("from Thread 1: " + Thread.currentThread());
                   lock.wait();
                   System.out.println("Message: " + message);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        });

        // notify() // mengirim sinyal komunikasi antar thread
        Thread th2 = new Thread(() -> {
            synchronized(lock){
                System.out.println("from Thread 2: " + Thread.currentThread());
                message = "this is message from th2";
                lock.notify();
            }
        });

        th1.start();
        th2.start();

        th1.join();
        th2.join();

    }

    @Test
    void testThreadCommunicationOtomatis2() throws InterruptedException {
        final Object lock = new Object();

        // wait() // menerima sinyal komunikasi antar thread
        Thread th1 = new Thread(() -> {
            synchronized (lock){
                try {
                    System.out.println("from Thread 1: " + Thread.currentThread());
                    lock.wait();
                    System.out.println("Message: " + message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // wait() // menerima sinyal komunikasi antar thread
        Thread th3 = new Thread(() -> {
            synchronized (lock){
                try {
                    System.out.println("from Thread 3: " + Thread.currentThread());
                    lock.wait();
                    System.out.println("Message: " + message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // wait() // menerima sinyal komunikasi antar thread
        Thread th4 = new Thread(() -> {
            synchronized (lock){
                try {
                    System.out.println("from Thread 4: " + Thread.currentThread());
                    lock.wait();
                    System.out.println("Message: " + message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // notifyAll() // mengirim sinyal ke semua komunikasi antar thread
        Thread th2 = new Thread(() -> {
            synchronized(lock){
                System.out.println("from Thread 2: " + Thread.currentThread());
                message = "this is message from th2";
                lock.notifyAll();
            }
        });

        th1.start();
        th2.start(); // thread2 yang memberi sinyal ke thread lain
        th3.start();
        th4.start();

        th1.join();
        th2.join();
        th3.join();
        th4.join();

    }

}
