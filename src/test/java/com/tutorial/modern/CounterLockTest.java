package com.tutorial.modern;

import com.tutorial.data.CounterLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class CounterLockTest {

    @Test
    void testCreateLocks() throws InterruptedException {

        CounterLock lock = new CounterLock();

        Runnable runnable = () -> {
            for (int i = 0; i < 1000000; i++) {
                lock.increment();
            }
        };

        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        log.info("Value: {}", lock.getValue());

    }

}
