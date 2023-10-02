package com.tutorial.modern.collection;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

@SpringBootTest
public class ConcurrentMapTest {

    @Test
    void testCreateConcurentMap() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(10);
        ConcurrentMap<Integer, String> integerStringConcurrentMap = new ConcurrentHashMap<>(); // ConcurrentHashMap<K,V> // object implement
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // menaruh data di ConcurrentMap
        for (int i = 0; i < 10; i++) {
            final int task = i;
            executorService.execute(() -> {
                try {
                    Thread.sleep(1000L);
                    integerStringConcurrentMap.putIfAbsent(task, "Data: " + UUID.randomUUID().toString());
//                    integerStringConcurrentMap.putIfAbsent(task, "Data: " + UUID.randomUUID().toString());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    countDownLatch.countDown(); // void countDown() // Mengurangi jumlah dari latch, melepaskan semua thread yang menunggu jika jumlah mencapai nol.
                }
            });
        }

        // mengambil data dari ConcurrentMap
        executorService.execute(() -> {
            try {
                countDownLatch.await(); // void await() // Menyebabkan thread saat ini menunggu hingga kait menghitung mundur hingga nol, kecuali thread tersebut diinterupsi .
                integerStringConcurrentMap.forEach(new BiConsumer<Integer, String>() {
                    @Override
                    public void accept(Integer integer, String s) {
                        System.out.println("'Key': " + integer + " : " + "'value' -> " + s);
                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);

    }

    @Test
    void testCollection(){

        /**
         * ● Pada kasus tertentu, kadang kita tetap butuh menggunakan Java Collection, namun butuh menggunakan multiple thread
         * ● Untuk kasus seperti itu, disarankan merubah Java Collection menjadi synchronized menggunakan
         *   helper method Collections.synchronized…(collection)
         */

        List<String> values = List.of("anak", "om", "mamat");
        List<String> synchronizedList = Collections.synchronizedList(values);
        // synchronizedList.add("halo bro"); // collection immutable  // Error at java.base/java.util.ImmutableCollections.uoe(ImmutableCollections.java:142)

        for (String value : synchronizedList){
            System.out.println(value);
        }

    }

}
