package com.tutorial.threadlegacy;

import com.tutorial.data.Balance;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class DeadLockTest {

    @Test
    void testDeadLock() throws InterruptedException {

        Balance from = new Balance(1000000L);
        Balance to = new Balance(1000000L);

        Thread threadFrom = new Thread(() -> {
            try {
                Balance.transferDeadlock(from, to, 500000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread threadTo = new Thread(() -> {
            try {
                Balance.transferDeadlock(to, from, 500000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        threadFrom.start();
        threadTo.start();

        threadFrom.join();
        threadTo.join();

        log.info("Balance From: {}", threadFrom.getName());
        log.info("Balance To: {}", threadTo.getName());

        /**
         * result:
         * // threadFrom dan ThreadTo akan saling menunggu satu sama lain.. karena berjalan secara Parallel (Synchornize),
         * // tidak akan berhenti sampai aplikasi di hentikan
         */

    }

    @Test
    void testDeadLockWithSolution() throws InterruptedException {

        Balance from = new Balance(1000000L);
        Balance to = new Balance(1000000L);

        Thread threadFrom = new Thread(() -> {
            System.out.println("Hello from threadFrom: " + Thread.currentThread().getName());
            try {
                Balance.transferSolution(from, to, 500000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread threadTo = new Thread(() -> {
            System.out.println("Hello from threadTo: " + Thread.currentThread().getName());
            try {
                Balance.transferSolution(to, from, 500000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        threadFrom.start();
        threadTo.start();

        threadFrom.join();
        threadTo.join();

        log.info("Balance From: {}", threadFrom.getName());
        log.info("Balance To: {}", threadTo.getName());
        log.info("Balance value From: {}", from.getValue());
        log.info("Balance value To: {}", to.getValue());

        /**
         * result:
         * // sesuai hasil ekpetasi
         * Hello from threadTo: Thread-2
         * Hello from threadFrom: Thread-1
         * 2023-09-28T11:23:21.866+07:00  INFO 11552 --- [           main] com.tutorial.threadlegacy.DeadLockTest                : Balance From: Thread-1
         * 2023-09-28T11:23:21.872+07:00  INFO 11552 --- [           main] com.tutorial.threadlegacy.DeadLockTest                : Balance To: Thread-2
         * 2023-09-28T11:23:21.872+07:00  INFO 11552 --- [           main] com.tutorial.threadlegacy.DeadLockTest                : Balance value From: 1000000
         * 2023-09-28T11:23:21.872+07:00  INFO 11552 --- [           main] com.tutorial.threadlegacy.DeadLockTest                : Balance value To: 1000000
         */

    }

}
