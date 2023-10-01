package com.tutorial.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Balance {

    private Long value;

    public static void transferDeadlock(Balance from, Balance to, Long value) throws InterruptedException {

        synchronized (from) {
            Thread.sleep(1000L);
            synchronized (to) {
                Thread.sleep(1000L);
                from.setValue(from.getValue() - value);
                to.setValue(to.getValue() + value);
            }
        }

    }

    public static void transferSolution(Balance from, Balance to, Long value) throws InterruptedException {

        // mengatasi DeadLock di buat terpisah synchronized statement nya
        synchronized (from) {
            Thread.sleep(1000L);
            from.setValue(from.getValue() - value);
        }
        synchronized (to) {
            Thread.sleep(1000L);
            to.setValue(to.getValue() + value);
        }

    }

}
