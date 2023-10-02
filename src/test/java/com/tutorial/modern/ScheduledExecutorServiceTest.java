package com.tutorial.modern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ScheduledExecutorServiceTest {

    // membuat eksekusi penjadwalan
    // secara delay (ditangguhkan) dan periodic

    @Test
    void testCreateScheduledDelayJob() throws InterruptedException {
        // task yang di delay(di tangguhkan)
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        ScheduledFuture<?> taskDelayJob = scheduledExecutorService.schedule(() -> {
            System.out.println("task Delay Job");
        }, 2, TimeUnit.SECONDS);// ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)

        System.out.println("Delay Millis: " + taskDelayJob.getDelay(TimeUnit.MILLISECONDS));
        scheduledExecutorService.awaitTermination(1, TimeUnit.DAYS);

    }

    @Test
    void testCreateScheduledPeriodicJob() throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        ScheduledFuture<?> taskPeriodicJob = scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println("task Periodic Job");
        }, 2, 2, TimeUnit.SECONDS);

        System.out.println("Delay Millis: " + taskPeriodicJob.getDelay(TimeUnit.MILLISECONDS));
        scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS);

    }

}
