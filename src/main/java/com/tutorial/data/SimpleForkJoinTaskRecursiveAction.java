package com.tutorial.data;

import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class SimpleForkJoinTaskRecursiveAction extends RecursiveAction {

    private List<Integer> integers;

    public SimpleForkJoinTaskRecursiveAction(List<Integer> integers) {
        this.integers = integers;
    }

    @Override
    protected void compute() {
        if (integers.size() <= 10){
            doCompute();
        } else {
            forkCompute();
        }
    }

    private void doCompute(){
        integers.forEach(value -> {
            System.out.println(Thread.currentThread().getName() + " : " + integers);
        });
    }

    private void forkCompute(){
        // Recursive: forkCompute() -> SimpleForkJoinTaskRecursiveAction(listArgs) -> forkCompute() -> SimpleForkJoinTaskRecursiveAction(listArgs) // sampai semua task di eksekusi
        List<Integer> integers1 = this.integers.subList(0, this.integers.size() / 2); // subList(int fromIndex, int toIndex) // Mengembalikan tampilan bagian daftar ini antara yang ditentukan fromIndex, inklusif, dan toIndexeksklusif.
        SimpleForkJoinTaskRecursiveAction task1 = new SimpleForkJoinTaskRecursiveAction(integers1);

        List<Integer> integers2 = this.integers.subList(this.integers.size() / 2, this.integers.size()); // List<E> subList(int fromIndex, int toIndex) // Mengembalikan tampilan bagian daftar ini antara yang ditentukan fromIndex, inklusif, dan toIndexeksklusif.
        SimpleForkJoinTaskRecursiveAction task2 = new SimpleForkJoinTaskRecursiveAction(integers2);

        ForkJoinTask.invokeAll(task1, task2); // static void invokeAll(ForkJoinTask<?> t1, ForkJoinTask<?> t2) // Mem-fork (garpu mencacah) tugas-tugas yang diberikan, kembali ketika isDone setiap tugas ditangguhkan(delay) atau ditemukan pengecualian (yang tidak dicentang), dalam hal ini pengecualian akan dimunculkan kembali.

    }
}
