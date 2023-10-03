package com.tutorial.data;

import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class SimpleForkJoinTaskRecursiveTask extends RecursiveTask<Long> {

    private List<Integer> integers;

    public SimpleForkJoinTaskRecursiveTask(List<Integer> integers) {
        this.integers = integers;
    }

    @Override
    protected Long compute() {
        if (integers.size() <= 10) {
            return doCompute();
        } else {
            return forkCompute();
        }
    }

    private Long doCompute() {
        return integers.stream()
                .mapToLong(value -> value)
                .peek(value -> {
                    System.out.println(Thread.currentThread().getName() + " : " + integers);
                }).sum();
    }

    private Long forkCompute() {
        // Recursive: forkCompute() -> SimpleForkJoinTaskRecursiveTask(listArgs) -> forkCompute() -> SimpleForkJoinTaskRecursiveTask(listArgs) // sampai semua task di eksekusi
        List<Integer> integers1 = this.integers.subList(0, this.integers.size() / 2); // subList(int fromIndex, int toIndex) // Mengembalikan tampilan bagian daftar ini antara yang ditentukan fromIndex, inklusif, dan toIndexeksklusif.
        SimpleForkJoinTaskRecursiveTask task1 = new SimpleForkJoinTaskRecursiveTask(integers1);

        List<Integer> integers2 = this.integers.subList(this.integers.size() / 2, this.integers.size()); // List<E> subList(int fromIndex, int toIndex) // Mengembalikan tampilan bagian daftar ini antara yang ditentukan fromIndex, inklusif, dan toIndexeksklusif.
        SimpleForkJoinTaskRecursiveTask task2 = new SimpleForkJoinTaskRecursiveTask(integers2);

        ForkJoinTask.invokeAll(task1, task2); // static void invokeAll(ForkJoinTask<?> t1, ForkJoinTask<?> t2) // Mem-fork (garpu mencacah) tugas-tugas yang diberikan, kembali ketika isDone setiap tugas ditangguhkan(delay) atau ditemukan pengecualian (yang tidak dicentang), dalam hal ini pengecualian akan dimunculkan kembali.
        return task1.join() + task2.join(); // menunggu task sampai di eksekusi semua
    }

}
