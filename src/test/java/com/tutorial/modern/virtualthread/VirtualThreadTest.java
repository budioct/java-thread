package com.tutorial.modern.virtualthread;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class VirtualThreadTest {

    @Test
    void testCreatePlatformThread() throws InterruptedException, IOException {

        /**
         * Java Thread, semua itu kita sebut adalah Platform Thread
         * Platform Thread merupakan wrapper untuk Thread Sistem Operasi, artinya setiap Platform Thread yang dibuat, secara otomatis akan dibuat Thread Sistem Operasi
         * Ada Builder Platform Thread yang bisa digunakan untuk mempermudah ketika kita membuat Platform Thread
         *
         * Builder: Thread.ofPlatform()
         * // jalan dengan Thread bawaan java main
         */

        for (int i = 0; i < 1000; i++) {
            final int task = i;
            Thread.ofPlatform()
                    .name("Palatform Thread")
                    .daemon(false)
                    .start(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(Duration.ofSeconds(2));
                                System.out.println("Task-" + task + " ,Thread: " + Thread.currentThread());
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }); // static Builder.OfPlatform ofPlatform()
        }

//        Thread.sleep(Duration.ofSeconds(4));
        System.in.read();

        /**
         * result:
         * Task-44 ,Thread: Thread[#78,Palatform Thread,5,main]
         * Task-85 ,Thread: Thread[#119,Palatform Thread,5,main]
         * Task-102 ,Thread: Thread[#136,Palatform Thread,5,main]
         * Task-87 ,Thread: Thread[#121,Palatform Thread,5,main]
         * Task-86 ,Thread: Thread[#120,Palatform Thread,5,main]
         */

    }

    @Test
    void testCreateVirtualThread() throws IOException, InterruptedException {

        /**
         * Builder: Thread.ofVirtual()
         * // jalan dengan Thread ForkJoinPool
         */

        for (int i = 0; i < 1000; i++) {
            final int task = i;
            Thread thread = Thread.ofVirtual()
                    .name("Virtual Thread")
                    .unstarted(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(Duration.ofSeconds(2));
                                System.out.println("Task-" + task + " ,Thread: " + Thread.currentThread());
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }); // static Builder.OfVirtual ofVirtual()
            thread.start();
        }

//        Thread.sleep(Duration.ofSeconds(4));
        System.in.read();

        /**
         * result:
         * Task-4 ,Thread: VirtualThread[#39,Virtual Thread]/runnable@ForkJoinPool-1-worker-8
         * Task-89 ,Thread: VirtualThread[#127,Virtual Thread]/runnable@ForkJoinPool-1-worker-1
         * Task-97 ,Thread: VirtualThread[#136,Virtual Thread]/runnable@ForkJoinPool-1-worker-7
         * Task-73 ,Thread: VirtualThread[#111,Virtual Thread]/runnable@ForkJoinPool-1-worker-4
         * Task-110 ,Thread: VirtualThread[#150,Virtual Thread]/runnable@ForkJoinPool-1-worker-2
         * Task-105 ,Thread: VirtualThread[#145,Virtual Thread]/runnable@ForkJoinPool-1-worker-3
         * Task-113 ,Thread: VirtualThread[#153,Virtual Thread]/runnable@ForkJoinPool-1-worker-6
         * Task-114 ,Thread: VirtualThread[#154,Virtual Thread]/runnable@ForkJoinPool-1-worker-5
         */
    }

    @Test
    void testCreateVirtualThreadWithExecutorService() throws InterruptedException, IOException {

        /**
         * Builder: Executors.newVirtualThreadPerTaskExecutor()
         */

        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

        for (int i = 0; i < 1000; i++) {
            final int task = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(Duration.ofSeconds(2));
                        System.out.println("Task-" + task + " ,Thread: " + Thread.currentThread());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

//        Thread.sleep(Duration.ofSeconds(4));
//        System.in.read();
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

        /**
         * result:
         * Task-1 ,Thread: VirtualThread[#36]/runnable@ForkJoinPool-1-worker-4
         * Task-221 ,Thread: VirtualThread[#263]/runnable@ForkJoinPool-1-worker-8
         * Task-225 ,Thread: VirtualThread[#267]/runnable@ForkJoinPool-1-worker-1
         * Task-220 ,Thread: VirtualThread[#262]/runnable@ForkJoinPool-1-worker-5
         * Task-227 ,Thread: VirtualThread[#269]/runnable@ForkJoinPool-1-worker-2
         * Task-230 ,Thread: VirtualThread[#272]/runnable@ForkJoinPool-1-worker-7
         * Task-233 ,Thread: VirtualThread[#275]/runnable@ForkJoinPool-1-worker-3
         * Task-234 ,Thread: VirtualThread[#276]/runnable@ForkJoinPool-1-worker-6
         */
    }

}
