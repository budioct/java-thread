package com.tutorial.threadlegacy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootTest
public class TimerAsynchronousTest {

    @Test
    void testTimerDelayJob() throws InterruptedException {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Delayed Task");
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 2000L); // void schedule(TimerTask task, long delay) // Schedules the specified task for execution after the specified delay.

        Thread.sleep(3000L); // menunggu proses taskTimer sampai selesai

    }

    @Test
    void testTimerPeriodicJob() throws InterruptedException {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timer Periodic");
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 1000L, 1000L); // void schedule(TimerTask task, long delay, long period) // Schedules the specified task for repeated fixed-delay execution, beginning after the specified delay.

        Thread.sleep(10000L); // menunggu proses taskTimer sampai selesai
    }

}
