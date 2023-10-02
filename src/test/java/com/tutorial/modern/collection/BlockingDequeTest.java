package com.tutorial.modern.collection;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.*;

@SpringBootTest
public class BlockingDequeTest {

    /**
     * BlockingDeque
     * ● BlockingDeque merupakan turunan dari BlockingQueue
     * ● BlockingDeque tidak hanya mendukung FIFO (first in first out), tapi juga LIFO (last in last out)
     * ● Implementasi BlockingDeque hanyalan LinkedBlockingDeque
     */

    @Test
    void testCreateBlockingDeque() throws InterruptedException {

        // data yang di terima sequential

        BlockingDeque<String> blockingDeque = new LinkedBlockingDeque<>();
        ExecutorService executorService = Executors.newFixedThreadPool(15);

        // menaruh data ke BlockingDeque
        executorService.execute(() -> {
            for (int i = 0; i < 10; i++) {
                final int task = i;
                try {
                    blockingDeque.putLast("Task-" + task + " ,saya menaruh data di sini dengan code: " + UUID.randomUUID().toString());
                    System.out.println("Success put data");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // mengambil data ke BlockingDeque
        executorService.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(2000L);
                    String received = blockingDeque.takeLast(); // E takeLast() // Mengambil dan menghapus elemen terakhir dari deque ini, menunggu jika perlu hingga elemen tersedia.
                    System.out.println("Received data: " + received);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

    }

    @Test
    void testCreateTransferQueue() throws InterruptedException {

        // data yang di terima sequential

        TransferQueue<String> linkedTransferQueue = new LinkedTransferQueue<>();
        ExecutorService executorService = Executors.newFixedThreadPool(15);

        // menaruh data ke BlockingDeque
        executorService.execute(() -> {
            for (int i = 0; i < 10; i++) {
                final int task = i;
                try {
                    linkedTransferQueue.transfer("Task-" + task + " ,saya menaruh data di sini dengan code: " + UUID.randomUUID().toString()); // void transfer(E e) // Mentransfer elemen ke konsumen, menunggu jika perlu untuk melakukannya.
                    System.out.println("Success put data");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // mengambil data ke BlockingDeque
        executorService.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(2000L);
                    String received = linkedTransferQueue.take(); // E take() // Mengambil dan menghapus kepala antrian yang diwakili oleh deque ini (dengan kata lain, elemen pertama dari deque ini), menunggu jika perlu hingga elemen tersedia.
                    System.out.println("Received data: " + received);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

    }

}
