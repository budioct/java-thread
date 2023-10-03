package com.tutorial.reactivestream;

import java.util.concurrent.Flow;

public class PrintSubscriber implements Flow.Subscriber<String> {

    /**
     * Flo.Publisher<T> adalah implementasi dari publisher (pengirim aliran data)
     * Flow.Subscriber<T> adalah implementasi dari subscriber (penerima aliran data)
     */

    private String name;

    private Long time;

    private Flow.Subscription subscription;

    public PrintSubscriber() {
    }

    public PrintSubscriber(String name, Long time) {
        this.name = name;
        this.time = time;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        try {
            Thread.sleep(1000);
            System.out.println(item + " ,Thread: " + Thread.currentThread().getName());
            this.subscription.request(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println(" ,Thread: " + Thread.currentThread().getName() + " DONE");
    }

}
