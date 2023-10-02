package com.tutorial.data;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CounterLock {

    private Long value = 0L;

    private final Lock reentrantLock = new ReentrantLock();

    /**
     * mirip seperti set synchronized statement yang di dalam block nya juga ada notifay dan await
     * synchronized()
     * notify()
     * await()
     */

    public void increment(){
        try{
            reentrantLock.lock(); // notify()
            value++;
        } finally {
            reentrantLock.unlock(); // await()
        }
    }

    public Long getValue(){
        return value;
    }

}
