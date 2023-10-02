package com.tutorial.modern.collection;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.*;

@SpringBootTest
public class BlockingQueueTest {

    @Test
    void testCreateBlockingQueue() throws InterruptedException {

        // ArrayBlockingQueue, implementasi BlockingQueue dengan ukuran fix
        // data yang di terima tidak sequential

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(5); // implement ArrayBlockingQueue<E> dari kontrak BlockingQueue<E>
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // menaruh data ke BlockingQueue
        for (int i = 0; i < 10; i++) {
            final int task = i;
            executorService.execute(() -> {
                try {
                    blockingQueue.put("Task-" + task + " ,saya menaruh data di sini dengan code:" + UUID.randomUUID().toString()); // void put(E e) // Memasukkan elemen tertentu ke dalam antrian yang diwakili oleh deque ini (dengan kata lain, di bagian belakang deque ini), menunggu jika perlu hingga ruang tersedia.
                    System.out.println("Success put data");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // mengambil data dari BlockingQueue
        executorService.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(500L);
                    String received = blockingQueue.take(); // E take() // Mengambil dan menghapus kepala antrian yang diwakili oleh deque ini (dengan kata lain, elemen pertama dari deque ini), menunggu jika perlu hingga elemen tersedia.
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
    void testCreateLinkedBlockingQueue() throws InterruptedException {

        // LinkedBlockingQueue, implementasi BlockingQueue dengan ukuran bisa berkembang
        // data yang di terima sequential

        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // menaruh data ke BlockingQueue
        for (int i = 0; i < 10; i++) {
            final int task = i;
            executorService.execute(() -> {
                try {
                    blockingQueue.put("Task-" + task + " ,saya menaruh data di sini dengan code:" + UUID.randomUUID().toString());
                    System.out.println("Success put data");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // mengambil data dari BlockingQueue
        executorService.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(500L);
                    String received = blockingQueue.take(); // E take() // Mengambil dan menghapus kepala antrian yang diwakili oleh deque ini (dengan kata lain, elemen pertama dari deque ini), menunggu jika perlu hingga elemen tersedia.
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
    void testCreatePriorityBlockingQueue() throws InterruptedException {

        // PriorityBlockingQueue, implementasi BlockingQueue dengan otomatis berurut berdasarkan prioritas
        // data yang di terima sequential

        BlockingQueue<String> blockingQueue = new PriorityBlockingQueue<>(10, Comparator.reverseOrder()); // reverse data dengan Comparator
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // menaruh data ke BlockingQueue
        for (int i = 0; i < 10; i++) {
            final int task = i;
            executorService.execute(() -> {
                try {
                    blockingQueue.put("Task-" + task + " ,saya menaruh data di sini dengan code:" + UUID.randomUUID().toString());
                    System.out.println("Success put data");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // mengambil data dari BlockingQueue
        executorService.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(500L);
                    String received = blockingQueue.take(); // E take() // Mengambil dan menghapus kepala antrian yang diwakili oleh deque ini (dengan kata lain, elemen pertama dari deque ini), menunggu jika perlu hingga elemen tersedia.
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
    void testCreateDelayQueue() throws InterruptedException {

        // DelayQueue, implementasi BlockingQueue untuk tipe data Delayed, dimana data tidak bisa diambil sebelum waktu delay yang telah ditentukan
        // data yang di terima sequential

        BlockingQueue<ScheduledFuture<String>> blockingQueue = new DelayQueue<>();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // menaruh data ke BlockingQueue
        for (int i = 1; i <= 10; i++) {
            final int task = i;
            blockingQueue.put(scheduledExecutorService.schedule(() -> "Task-" + task + " ,saya menaruh data di sini dengan code:" + UUID.randomUUID().toString(), i, TimeUnit.SECONDS)); // ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
            System.out.println("Success put data");
        }

        // mengambil data dari BlockingQueue
        executorService.execute(() -> {
            while (true) {
                try {
                    ScheduledFuture<String> received = blockingQueue.take(); // E take() // Mengambil dan menghapus kepala antrian yang diwakili oleh deque ini (dengan kata lain, elemen pertama dari deque ini), menunggu jika perlu hingga elemen tersedia.
                    System.out.println("Received data: " + received.get()); // V get() // Menunggu jika perlu hingga penghitungan selesai, lalu mengambil hasilnya.
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

    }

    @Test
    void testCreateSynchronousQueue() throws InterruptedException {

        // SynchronousQueue, implementasi BlockingQueue dimana thread yang menambah data harus menunggu sampai ada thread yang mengambil data, begitu juga kebalikannya
        // data yang di terima tidak sequential

        BlockingQueue<String> blockingQueue = new SynchronousQueue<>();
        ExecutorService executorService = Executors.newFixedThreadPool(11);

        // menaruh data ke BlockingQueue
        for (int i = 0; i < 10; i++) {
            final int task = i;
            executorService.execute(() -> {
                try {
                    blockingQueue.put("Task-" + task + " ,saya menaruh data di sini dengan code:" + UUID.randomUUID().toString());
                    System.out.println("Success put data");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // mengambil data dari BlockingQueue
        executorService.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(2000L);
                    String received = blockingQueue.take(); // E take() // Mengambil dan menghapus kepala antrian yang diwakili oleh deque ini (dengan kata lain, elemen pertama dari deque ini), menunggu jika perlu hingga elemen tersedia.
                    System.out.println("Received data: " + received);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

//        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

    }


}
