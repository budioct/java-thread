package com.tutorial.threadlegacy;

import com.tutorial.data.Counter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class RaceConditionTest {

    @Test
    void testRaceCondition() throws InterruptedException {

        Counter counter = new Counter();

        Runnable runnable = () -> {
            for (long i = 0L; i < 10000000L; i++) {
                counter.increment();
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

        log.info("value counter: {}", counter.getValue());
        /**
         * result Race condition:
         * 2023-09-28T10:48:59.354+07:00  INFO 860 --- [           main] com.tutorial.RaceCondition               : value counter: 11256017
         * // hasil tidak sesuai ekpetasi
         */

    }

    @Test
    void testRaceConditionWithSynchronizedMethod() throws InterruptedException {

        Counter counter = new Counter();

        Runnable runnable = () -> {
            for (long i = 0L; i < 10000000L; i++) {
                counter.incrementSynchronizedMethod();
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

        log.info("value counter: {}", counter.getValue());
        /**
         * result Race condition:
         * 2023-09-28T10:57:02.396+07:00  INFO 7824 --- [           main] com.tutorial.RaceCondition               : value counter: 30000000
         * // hasil sesuai ekpetasi
         */

    }

    @Test
    void testRaceConditionWithSynchronizedStatement() throws InterruptedException {

        Counter counter = new Counter();

        Runnable runnable = () -> {
            for (long i = 0L; i < 10000000L; i++) {
                counter.incrementSynchronizedStatement();
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

        log.info("value counter: {}", counter.getValue());
        /**
         * result Race condition:
         * 2023-09-28T11:03:22.131+07:00  INFO 3136 --- [           main] com.tutorial.RaceCondition               : value counter: 30000000
         * // hasil sesuai ekpetasi
         */

    }


}
