package com.tutorial.modern;

import com.tutorial.data.CompletableFutureApp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

@SpringBootTest
public class CompletableFutureTest {

    /**
     * CompletableFuture
     * membuat Future<T> secara manual tidak perlu task Callable<T>
     * dengan set method
     * complete(value)                  uutuk value
     * completeExceptionally(error)     untuk error
     */

    CompletableFutureApp completableFutureApp = new CompletableFutureApp();

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
