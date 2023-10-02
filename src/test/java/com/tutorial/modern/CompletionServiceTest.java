package com.tutorial.modern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.concurrent.*;

@SpringBootTest
public class CompletionServiceTest {

    /**
     * CompletionService (jembatan)
     * CompletionService seubah interface untuk memisahkan antara bagian yang mengekseskusi asynchronous task dan menerima hasil task
     * <p>
     * Implementasi interface CompletionService adalah class ExecutorCompletionService
     */

    private Random random = new Random();

    @Test
    void testCreateCompletionService() throws InterruptedException {
        // CompletionService yang sudah ada executor thread nya
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        ExecutorCompletionService<String> service = new ExecutorCompletionService<>(executorService);

        // task yang di eksekusi
        Executors.newSingleThreadExecutor().execute(() -> {
            for (int i = 0; i < 100; i++) {
                final int task = i;
                service.submit(() -> {
                    Thread.sleep(random.nextInt(2000));
                    return "Task- " + task;
                });
            }
        });

        // pool menerima hasil task yang di eksekusi
        Executors.newSingleThreadExecutor().execute(() -> {
            while (true) {
                try {
                    Future<String> future = service.poll(5, TimeUnit.SECONDS);
                    if (future == null) {
                        break;
                    } else {
                        System.out.println(future.get());
                    }
                } catch (InterruptedException | ExecutionException e) {
//                    throw new RuntimeException(e);
                    break;
                }
            }
        });

//        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

    }


}
