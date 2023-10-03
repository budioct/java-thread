package com.tutorial.reactivestream;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class HelloProccessor extends SubmissionPublisher<String> implements Flow.Processor<String, String> {

    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1); // akan mengirim data ke method void onNext(String item)
    }

    @Override
    public void onNext(String item) {
        String value = "Hello onNext() " + item;
        submit(value); // int submit(T item)
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        close(); // void close()
    }
}

