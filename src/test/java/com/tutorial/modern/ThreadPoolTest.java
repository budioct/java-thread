package com.tutorial.modern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ThreadPoolTest {

    /**
     * Note: sebenarnya kita jarang membuat ThreadPoolExecutor jika memang kita ada kasus kebutuhan tuning ThreadPool
     */

    @Test
    void testCheckHardware() {

        int processors = Runtime.getRuntime().availableProcessors();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();

        System.gc(); // clear garbage collection

        System.out.println(processors);
        System.out.println(freeMemory);
        System.out.println(maxMemory);
        System.out.println(totalMemory);

    }

    @Test
    void testCreateThreadPool() {
        var minThread = 10;
        var maxThread = 100;
        var alive = 1;
        var aLifeTime = TimeUnit.MINUTES;
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100); // stack queue

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(minThread, maxThread, alive, aLifeTime, queue); // threadpool
    }

    @Test
    void testExecuteThreadPool() throws InterruptedException {
        var minThread = 10;
        var maxThread = 100;
        var alive = 1;
        var aLifeTime = TimeUnit.MINUTES;
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100); // stack queue

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(minThread, maxThread, alive, aLifeTime, queue); // threadpool

        threadPoolExecutor.execute(() -> {
            try {
                Thread.sleep(2000L);
                System.out.println("From thread: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(3000L);

    }

    @Test
    void testShutdownThreadPool() throws InterruptedException {

        /**
         * Cara kerja: Secara otomatis data runnable akan dikirim ke queue threadpool untuk dieksekusi oleh thread yang terdapat di threadpool
         *
         * Note : stack queue harus lebih besar dari task pengujian jika tidak mau error
         * java.util.concurrent.RejectedExecutionException: Task com.tutorial.modern.ThreadPoolTest$1@36920bd6 rejected from java.util.concurrent.ThreadPoolExecutor@235d659c[Running, pool size = 100, active threads = 100, queued tasks = 1000, completed tasks = 0]
         * jadi untuk meminimalisir stack queue harus sama denga task atau task lebih kecil dari stack queue
         */

        var minThread = 10; // eksekusi thread
        var maxThread = 100; // eksekusi max thread
        var alive = 1;
        var aLifeTime = TimeUnit.MINUTES;
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1000); // stack queue

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(minThread, maxThread, alive, aLifeTime, queue); // threadpool

        for (int i = 0; i < 1000; i++) {
            final int task = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000L);
                        System.out.println("task: " + task + " From Thread: " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }; // task
            threadPoolExecutor.execute(runnable); // void execute(Runnable command) // eksekusi task
        } // count task

        Thread.sleep(10_000L);
        // mematikan thread pool
        threadPoolExecutor.shutdown(); // shutdown() // menghentikan task secara tertib dimana tugas yang diserahkan sebelumnya dijalankan, namun tidak dengan task yang baru yang akan di terima
//        threadPoolExecutor.shutdownNow(); // shutdownNow() // menhentikan semua tugas di serahkan, menghentikan pemerosesan task yang menunggu, dan return list task yang menunggu di eksekusi
//        threadPoolExecutor.awaitTermination(1, TimeUnit.DAYS) // menunggu semua task hingga pemerosesan selesai dengan batas waktu yang terjadi

    }

    public static class LogRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("Task: " + r + " is rejected"); // print exception handler jika stack queue dan thread penuh
        }
    }

    @Test
    void testShutdownThreadPoolWithRejectedHandler() throws InterruptedException {

        /**
         * Cara kerja: Secara otomatis data runnable akan dikirim ke queue threadpool untuk dieksekusi oleh thread yang terdapat di threadpool
         */

        var minThread = 10; // eksekusi thread
        var maxThread = 100; // eksekusi max thread
        var alive = 1;
        var aLifeTime = TimeUnit.MINUTES;
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10); // stack queue
        LogRejectedExecutionHandler handler = new LogRejectedExecutionHandler(); // Thread pool yang belum di tampung di stack queue dan di eksekusi threadPool

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(minThread, maxThread, alive, aLifeTime, queue, handler); // threadpool

        for (int i = 0; i < 1000; i++) {
            final int task = i;
            threadPoolExecutor.execute(() -> {
                try {
                    Thread.sleep(1000L);
                    System.out.println("task: " + task + " From Thread: " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }); // void execute(Runnable command) // eksekusi task
        } // count task

        Thread.sleep(5000L);
        threadPoolExecutor.shutdown();

    }

}

