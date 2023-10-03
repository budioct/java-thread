package com.tutorial.modern.reactivestream;

import com.tutorial.reactivestream.HelloProccessor;
import com.tutorial.reactivestream.PrintSubscriber;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ReactiveTest {

    @Test
    void testPublisher() throws InterruptedException {

        ExecutorService executorService = Executors.newSingleThreadExecutor(); // buat threadPool

        SubmissionPublisher<String> publisher = new SubmissionPublisher<>(); // instance object publisher
        PrintSubscriber subscriber1 = new PrintSubscriber("Data 1", 1000L); // instance object subscriber
        PrintSubscriber subscriber2 = new PrintSubscriber("Data 2", 500L); // instance object subscriber

        publisher.subscribe(subscriber1);
        publisher.subscribe(subscriber2);


        executorService.execute(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(2000L);
                    publisher.submit("TASK-publisher-" + i); // int submit(T item) // akan mengirim ke subscreber di object implement Flow.Subscriber<T>.onNext(String item)
                    System.out.println(Thread.currentThread().getName() + ": Send-publisher: " + i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

//        Thread.sleep(100 * 1000L);

        /**
         * Flow Publisher<T>. publisher akan mengirim per task ke subscriber kemudian baru di eksekusi oleh reactive
         * result:
         * pool-2-thread-1: Send-publisher: 0
         * TASK-publisher-0 ,Thread: ForkJoinPool.commonPool-worker-2
         * TASK-publisher-0 ,Thread: ForkJoinPool.commonPool-worker-1
         * pool-2-thread-1: Send-publisher: 1
         * TASK-publisher-1 ,Thread: ForkJoinPool.commonPool-worker-1
         * TASK-publisher-1 ,Thread: ForkJoinPool.commonPool-worker-2
         * pool-2-thread-1: Send-publisher: 2
         * TASK-publisher-2 ,Thread: ForkJoinPool.commonPool-worker-1
         * TASK-publisher-2 ,Thread: ForkJoinPool.commonPool-worker-2
         */
    }

    @Test
    void testCreatePublisherWithBuffer() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        PrintSubscriber subscriber1 = new PrintSubscriber("Data 1", 1000L); // instance object subscriber
        PrintSubscriber subscriber2 = new PrintSubscriber("Data 2", 500L); // instance object subscriber

        publisher.subscribe(subscriber1);
        publisher.subscribe(subscriber2);

        executorService.execute(() -> {
            for (int i = 0; i < 1000; i++) {
                //Thread.sleep(2000L);
                publisher.submit("TASK-publisher-" + i); // int submit(T item) // akan mengirim ke subscreber di object implement Flow.Subscriber<T>.onNext(String item)
                System.out.println(Thread.currentThread().getName() + ": Send-publisher: " + i);
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

//        Thread.sleep(100 * 1000L);

        /**
         * Flow Publisher dengan Buffer itu publisher akan mengirim task dengan max count 256 ke subscriber kemudian baru di eksekusi oleh reactive
         * perlu di ingat jika ingin implementasi Buffer (antrian) nilai max nya DEFAULT_BUFFER_SIZE = 256.. hanya 256 saja untuk eksekusi Reactive Stream
         */

    }

    @Test
    void testCreatePublisherWithProccessor() throws InterruptedException {

        ExecutorService executorService = Executors.newWorkStealingPool();
//        ExecutorService executorService = Executors.newFixedThreadPool(4);

        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        HelloProccessor subscreberProccessor = new HelloProccessor();
        PrintSubscriber subscriber1 = new PrintSubscriber("Data 1", 500L); // instance object subscriber

        publisher.subscribe(subscreberProccessor);
        subscreberProccessor.subscribe(subscriber1);

        executorService.execute(() -> {
            for (int i = 0; i < 100; i++) {
                publisher.submit("TASK-publisher-proccessor" + i); // int submit(T item) // akan mengirim ke subscreber di object implement Flow.Subscriber<T>.onNext(String item)
                System.out.println(Thread.currentThread().getName() + ": Send-publisher: " + i);
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

        Thread.sleep(100 * 500L);

        /**
         * Flow Processor itu publisher akan mengirim semua task ke subscriber kemudian baru di eksekusi oleh reactive
         */

    }

}
