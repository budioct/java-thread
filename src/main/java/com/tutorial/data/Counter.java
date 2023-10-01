package com.tutorial.data;

public class Counter {

    private Long value = 0L;

    public synchronized void incrementSynchronizedMethod() {
        // synchronized method // secara otomatis method tersebut hanya bisa diakses oleh satu thread pada satu waktu
        // Synchronization fitur dimana kita memaksa kode program hanya boleh diakses dan dieksekusi oleh satu thread saja
        value++;
    }

    public void incrementSynchronizedStatement() {
        // synchronized statement // mirip dengan synchronized method
        // perbedaanya hanya kita harus menentukan object intrinsic lock sendiri
        synchronized (this) {
            value++;
        }
    }

    public void increment() {
        value++;
    }

    public void decrement() {
        value--;
    }

    public Long getValue() {
        return value;
    }

}
