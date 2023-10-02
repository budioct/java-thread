package com.tutorial.data;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CounterReadWriteLock {

    // mirip seperti counterLock, yang membedakan hanya antara operasi update dan operasi get

    private Long value = 0L;

    final private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * mirip seperti set synchronized statement yang di dalam block nya juga ada notifay dan await
     * synchronized()
     * notify()
     * await()
     */

    public void increment() {
        try {
            readWriteLock.writeLock().lock();
            value++;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public Long getValue() {
        try {
            readWriteLock.readLock().lock();
            return value;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

}
