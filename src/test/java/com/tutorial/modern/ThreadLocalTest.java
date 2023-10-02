package com.tutorial.modern;

import com.tutorial.data.UserServiceApp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ThreadLocalTest {

    /**
     * ● ThreadLocal merupakan fitur di Java untuk menyimpan data
     * ● ThreadLocal memberi kita kemampuan untuk menyimpan data yang hanya bisa digunakan di thread tersebut
     * ● Tiap thread akan memiliki data yang berbeda dan tidak saling terhubung antar thread
     */

    @Test
    void testCreateThreadLocal() throws InterruptedException {

        UserServiceApp userServiceApp = new UserServiceApp();
        Random random = new Random();
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        for (int i = 0; i < 50; i++) {
            final int task = i;
            executorService.execute(() -> {
                try {
                    userServiceApp.setUser("User-" + task); // set data ke LocalThread
                    Thread.sleep(1000 + random.nextInt(3000));
                    userServiceApp.doAction(); // get data dari LocalThread
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

    }

}
