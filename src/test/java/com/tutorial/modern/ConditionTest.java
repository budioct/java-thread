package com.tutorial.modern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootTest
public class ConditionTest {

    private String message = null;

    @Test
    void testConditionSingle() throws InterruptedException {

        Lock reentrantLock = new ReentrantLock(); // ReentrantLock implements from Lock thread modern for sychronouse method and sychronouse statement
        Condition condition = reentrantLock.newCondition(); // Condition newCondition()

        // menerima signal() dengan await(), get value message
        Thread thread1 = new Thread(() -> {
            try {
                reentrantLock.lock();
                condition.await();
                System.out.println("Thread-1: " + message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                reentrantLock.unlock();
            }
        });

        // set value message, setelah itu kirim signal()
        Thread thread2 = new Thread(() -> {
            try {
                reentrantLock.lock();
                message = "this message from thread2";
                condition.signal();
            } finally {
                reentrantLock.unlock();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

    }

    @Test
    void testConditionMulti() throws InterruptedException {

        Lock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();

        // menerima signal() dengan await(), get value message
        Thread thread1 = new Thread(() -> {
            try {
                reentrantLock.lock();
                condition.await();
                System.out.println("Thread-1: " + message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                reentrantLock.unlock();
            }
        });

        // menerima signal() dengan await(), get value message
        Thread thread2 = new Thread(() -> {
            try {
                reentrantLock.lock();
                condition.await();
                System.out.println("Thread-2: " + message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                reentrantLock.unlock();
            }
        });

        // menerima signal() dengan await(), get value message
        Thread thread3 = new Thread(() -> {
            try {
                reentrantLock.lock();
                condition.await();
                System.out.println("Thread-3: " + message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                reentrantLock.unlock();
            }
        });

        // menerima signal() dengan await(), get value message
        Thread thread4 = new Thread(() -> {
            try {
                reentrantLock.lock();
                condition.await();
                System.out.println("Thread-4: " + message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                reentrantLock.unlock();
            }
        });

        // set value message, setelah itu kirim signalAll()
        Thread thread5 = new Thread(() -> {
            try {
                reentrantLock.lock();
                message = "this message from thread5";
                condition.signalAll();
            } finally {
                reentrantLock.unlock();
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();

    }

}
