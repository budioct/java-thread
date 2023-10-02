package com.tutorial.modern;

import com.tutorial.data.Counter;
import com.tutorial.data.CounterAtomic;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class CounterAtomicTest {

    @Test
    void testCounterAtomic() throws InterruptedException {

        CounterAtomic atomic = new CounterAtomic(); // jika di jalankan thread sudah concurency

        Runnable runnable = () -> {
            for (int i = 0; i < 1000000; i++) {
                atomic.increment();
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

        log.info("value Atomic counter: {}", atomic.getValue());

        /**
         * ekpetasi adalah 3_000_000 karena parallel sudah di concurency
         */

    }

    @Test
    void testCounter() throws InterruptedException {

        Counter counter = new Counter(); // belum concurency

        Runnable runnable = () -> {
            for (int i = 0; i < 1000000; i++) {
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
         * ekpetasi adalah 3_000_000 karena parallel sudah di concurency
         */

    }

}
