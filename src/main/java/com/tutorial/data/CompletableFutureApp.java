package com.tutorial.data;

import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Function;

public class CompletableFutureApp {

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public Future<String> getValue() {

        CompletableFuture<String> future = new CompletableFuture<>();

        executorService.execute(() -> {
            try {
                Thread.sleep(2000);
                future.complete("Success");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return future;

    }

    public CompletableFuture<String> getValue2() {

        CompletableFuture<String> future = new CompletableFuture<>();

        executorService.execute(() -> {
            try {
                Thread.sleep(2000);
                future.complete("CompletableFutureApp with method getValue2()");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return future;

    }

    public void execute(CompletableFuture<String> future, String value) {

        Random random = new Random();

        executorService.execute(() -> {
            try {
                Thread.sleep(1000L + random.nextInt(5000));
                future.complete(value);
            } catch (InterruptedException e) {
                future.completeExceptionally(e);
            }
        });

    }

    public Future<String> getFastest() {

        CompletableFuture<String> future = new CompletableFuture<>();

        execute(future, "Thread 1");
        execute(future, "Thread 2");
        execute(future, "Thread 3");

        return future;
    }

    public void getCompletionStage() throws ExecutionException, InterruptedException {

        CompletableFuture<String> future1 = getValue2();

        // lambda
        CompletableFuture<String[]> future2 = future1
                .thenApply((String string) -> string.toUpperCase())
                .thenApply((String string) -> string.split(" "));

        // anonymouse class
        CompletableFuture<String[]> future3 = future1.thenApply(new Function<String, String>() {
            @Override
            public String apply(String string) {
                return string.toUpperCase();
            }
        }).thenApply(new Function<String, String[]>() {
            @Override
            public String[] apply(String string) {
                return string.split(" ");
            }
        });

        String[] data = future3.get(); // T get() throws InterruptedException, ExecutionException

        for (String string : data) {
            System.out.println(string);
        }

    }

}
