package com.tutorial.modern;

import com.tutorial.data.CompletableFutureApp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@SpringBootTest
public class CompletableFutureTest {

    /**
     * CompletableFuture (Non-blocking)
     * membuat Future<T> secara manual tidak perlu task Callable<T>
     * dengan set method
     * complete(value)                  uutuk value
     * completeExceptionally(error)     untuk error
     * <p>
     * * supplyAsync(Supplier<U>) : menjalankan tugas secara asinkron dan mengembalikan CompletableFuture yang mewakili hasilnya
     * * thenApply(Function<T,R>) : menerapkan fungsi pada hasil tugas sebelumnya dan mengembalikan CompletableFuture yang mewakili hasil transformasi
     * * thenAccept(Consumer<T>) : cetak hasil transform compute
     * * thenCompose() : mengeksekusi tugas yang mengembalikan CompletableFuture dan mengembalikan CompletableFuture yang mewakili hasil tugas bersarang
     * * allOf() : menjalankan beberapa tugas secara paralel dan mengembalikan CompletableFuture yang mewakili penyelesaian semua tugas
     * <p>
     * https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CompletableFuture.html
     */

    CompletableFutureApp completableFutureApp = new CompletableFutureApp();

    @Test
    void testCreateNonBlocking1() {
        CompletableFuture<Void> result = CompletableFuture.supplyAsync(() -> "this non-blocking")
                .thenApply(s -> Integer.valueOf(s.length()))
                .thenAccept(s -> log.info(String.valueOf(s)));// cetak panjang value string

        Assertions.assertNotNull(result);
    }

    @Test
    void testCreateNonBlocking2() throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> result = CompletableFuture.supplyAsync(new Supplier<String>() {
                    @Override
                    public String get() {
                        return "task dari user";
                    }
                })
                .thenApply(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) {
                        return Integer.valueOf(s.length());
                    }
                });

        Assertions.assertNotNull(result);
        Assertions.assertEquals(14, result.get()); // T get() // Menunggu jika perlu hingga masa depan ini selesai, lalu mengembalikan hasilnya.

    }

    @Test
    void testCreateNonBlocking3() throws ExecutionException, InterruptedException {

        CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
            return "Hello";
        }).thenApply((data) -> {
            try {
                Thread.sleep(Duration.ofSeconds(2));
                System.out.println("Thread: " + Thread.currentThread());
                return data + " World";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).thenApply(data -> data.toLowerCase());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("hello world", result.get()); // join() // Mengembalikan nilai hasil saat selesai, atau memunculkan pengecualian (tidak dicentang) jika diselesaikan dengan luar biasa.
    }

    @Test
    void testNonBlocking() throws InterruptedException {

        /**
         * Stream                           CompletableFuture
         * map(Function<T, R>)              thenApply
         * ForEach(Consumer<T>)             thenAccept
         */

        CompletableFutureApp.create(-4)
                .thenApply(data -> (Integer) (data + 1))
                .thenAccept(System.out::println) // save dataToDB(data)
                .thenRun(() -> System.out.println("log some info"))
                .thenRun(() -> System.out.println("some info op"))
        ;

        System.out.println("started the computation " + Thread.currentThread());

        Thread.sleep(2000);

    }

    @Test
    void testNonBlockingWithException() throws InterruptedException {

        /**
         * Stream                           CompletableFuture
         * map(Function<T, R>)              thenApply
         * ForEach(Consumer<T>)             thenAccept
         * Function<Throwable, ? extends T> exceptionally
         */

        // create(int n).. n <= 0
        CompletableFutureApp.create(-4)
                .thenApply(data -> (Integer) (data + 1))
                .exceptionally(error -> {
                    System.out.println(error);
//                    return (Integer) 100;

                    throw new RuntimeException("this is beyond repair");
                }) // catch input false
                .thenAccept(System.out::println) // save dataToDB(data)
                .thenRun(() -> System.out.println("log some info"))
                .thenRun(() -> System.out.println("some info op"))
                .exceptionally(error -> {
                    System.out.println(error);
                    throw new RuntimeException("Sorry");
                }) // catch error from exceptionally()
        ;

    }

    @Test
    void testNonBlockingWithCombine() throws InterruptedException {

        /**
         * Stream                CompletableFuture
         * map(f11)              Stream<R>
         * map(f1n)              Stream<List<R>>
         *
         * note:
         * if your function returns data, use thenApply
         * if your function returns a CF, use thenCompose
         */

        // create(int n).. n <= 0
        // combine common
//        CompletableFuture<Integer> cf1 = CompletableFutureApp.create(2);// 4
//        CompletableFuture<Integer> cf2 = CompletableFutureApp.create(3);// 6
//        cf1.thenCombine(cf2, (data1, data2) -> (Integer) (data1 + data2))
//                .thenAccept(System.out::println); // 10

        // combine with stream
        CompletableFutureApp.create(2)
//                .thenApply(data -> CompletableFutureApp.create(data))
                .thenCompose(data -> CompletableFutureApp.create(data))
                .thenAccept(System.out::println)
        ;


    }

    @Test
    void testCreateCompletableFuture() throws ExecutionException, InterruptedException {

        Future<String> future = completableFutureApp.getValue(); // Future<String> getValue()
        System.out.println(future.get());

    }

    @Test
    void testCreateCompletableFuture2() throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = completableFutureApp.getValue2();
        System.out.println(future.get());

    }

    @Test
    void testCreateCompletableExecute() throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = new CompletableFuture<>();
        completableFutureApp.execute(future, "this execute method create from test");
        System.out.println(future.get());

    }

    @Test
    void testCreateCompletableFastest() throws ExecutionException, InterruptedException {

        Future<String> fastest = completableFutureApp.getFastest();
        System.out.println(fastest.get());

    }

    @Test
    void testCreateCompleteStagegetCompletionStage() throws ExecutionException, InterruptedException {

        /**
         * CompletionStage
         * * CompletableFuture implementasi dari interface CompletionStage adalah operasi untuk mengubah atau transformasi datanya.. mirip operasi Stream<T> walaupun tidak komplit
         * * CompletionStage bisa menambahkan asynchronouse computation, tanpa harus menunggu dulu data dari Future nya ada
         *
         */

        completableFutureApp.getCompletionStage();

    }

}
