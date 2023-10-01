package com.tutorial.modern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class ExecutorsServiceTest {

    /**
     * Note: Executors adalah class utilitis implement otomatis dari ExecutorService, tidak pusing pusing tuning seperti class ThreadPoolExecutor
     * <p>
     * Executors Static Method      Keterangan
     * newFixedThreadPool(n)        Membuat threadpool dengan jumlah pool min dan max fix
     * newSingleThreadExecutor()    Membuat threadpool dengan jumlah pool min dan max 1
     * newCacheThreadPool()         Membuat threadpool dengan jumlah thread bisa bertambah tidak terhingga
     * <p>
     * Method eksekusi
     * void execute(Runnable task)
     * <T> Future<T> submit(Callable<T> task)
     */

    @Test
    void testCreateExecutorsService() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 100; i++) {
            final int task = i;
            executorService.execute(() -> {
                try {
                    Thread.sleep(500L);
                    System.out.println("Task:" + task + " ,Execute " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }); // void execute(Runnable command) // ekseuksi task
        }

//        Thread.sleep(10000L); // menunggu manual
        executorService.shutdown(); // menutup threadpool
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS); // menunggu task yang di eksekusi hingga selesai

    }

    /**
     * Callable<T>
     * Runnable dan Callable sama tetapi perbedanya Runnable tidak return value sedangkan Callable return value, callable sendiri adalah object generic
     * <p>
     * Future<T>
     * Future merupakan representasi data yang akan dikembalikan oleh proses asynchronous
     * Future, kita bisa mengecek apakah task Callable sudah selesai atau belum, dan juga return data hasil dari Callable
     * <p>
     * Future Method                    Keterangan
     * T get()                          Mengambil result data, jika belum ada, maka akan menunggu sampai ada
     * T get(timeout, time unit)        Mengambil result data, jika belum ada, maka akan menunggu sampai timeout
     * void cancel(mayInterrupt)        Membatalkan proses callable, dan apakah diperbolehkan di interrupt jika sudah terlanjur berjalan
     * boolean isCancelled()            Mengecek apakah future dibatalkan
     * boolean isDone()                 Mengecek apakah future telah selesai
     */

    @Test
    void testCreateCallableFuture() throws InterruptedException, ExecutionException {

        ExecutorService executorService = Executors.newFixedThreadPool(4);

//        Future<String> future = executorService.submit(() -> {
//            Thread.sleep(5000);
//            return "Hi";
//        }); // assaigment callable polimorpsm return Future

        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(5000);
                return "Hi";
            }
        }; // create task callable with anonymouse class

        Future<String> future = executorService.submit(callable); // assaigment callable polimorpsm return Future

        while (!future.isDone()) {
            System.out.println("Waiting Result!!");
            Thread.sleep(1000);
        } // interval await result

        System.out.println("Result: " + future.get());
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);

    }

    @Test
    void testCancleCallableFuture() throws InterruptedException, ExecutionException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(() -> {
            Thread.sleep(3000);
            return "This Task Callable!";
        });

        Thread.sleep(2000);
        future.cancel(true); // boolean cancel(boolean value) // Upaya untuk membatalkan pelaksanaan tugas ini.

        System.out.println("Apakah Future Dibatalkan: " + future.isCancelled());
        System.out.println("Result: " + future.get());

    }

    /**
     * Invoke All (Collection<Callable<T>>)
     * digunakan untuk mengeksekusi banyak callable sekaligus, hal ini bisa mempercepat proses asynchronous eksekusi task
     * invokeAll(Collection<Callable<T>>)       mengeksekusi banyak task sekaligus dalam bentuk collection
     *
     * Invoke Any (Collection<Callable<T>>)
     * untuk mendapatkan result yang paling cepat dalam proses asynchronous
     * invokeAny()           return result data dari Callable yang paling cepat
     */

    @Test
    void createInvokeAllCallableFuture() throws InterruptedException, ExecutionException {

        ExecutorService executorService = Executors.newFixedThreadPool(10); // ThreadPool

        List<Callable<String>> callableList1 = IntStream.range(1, 11)
                .mapToObj(operand -> (Callable<String>) () -> {
                    Thread.sleep(operand * 500L);
                    return String.valueOf(operand);
                }).collect(Collectors.toList()); // implement IntStream assaigment callable wrapper collection (lambda)

        List<Callable<String>> callableList2 = IntStream.range(1, 11)
                .mapToObj(value -> new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        Thread.sleep(value * 500L);
                        return String.valueOf(value);
                    }
                }).collect(Collectors.toList()); // implement IntStream assaigment callable wrapper collection (lambda)

        List<Future<String>> futures = executorService.invokeAll(callableList2); // <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) // execution return future wrapper collection

        for (Future<String> stringFuture : futures) {
            System.out.println("Counter: " + stringFuture.get());
        } // iteration data from future

    }

    @Test
    void testInvokeAnyCallableFuture() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Callable<String>> callableList = IntStream.range(1, 11)
                .mapToObj(value -> (Callable<String>) () -> {
                    Thread.sleep(value * 500L);
                    return String.valueOf(value);
                }).toList();

        String result = executorService.invokeAny(callableList); // <T> T invokeAny(Collection<? extends Callable<T>> tasks) // return data langsung type data nya
        System.out.println(result);

    }

}
