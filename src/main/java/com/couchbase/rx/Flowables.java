// File: src/main/java/com/couchbase/rx/Flowables.java
package com.couchbase.rx;

import java.util.ArrayList;
import java.util.List;

import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.functions.Functions;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

import com.couchbase.Util.ComputeFunction;

public class Flowables {
    public static void main(String[] args) {
        
        Flowable
        .range(1, 200)
        .observeOn(Schedulers.computation())
        .subscribe(ComputeFunction::compute);

        /*
        PublishProcessor<Integer> source = PublishProcessor.create();

        source
        //.observeOn(Schedulers.computation())
        .subscribe(ComputeFunction::compute, Throwable::printStackTrace, Functions.EMPTY_ACTION, s -> s.request(10));

        for (int i = 0; i < 1_000_000; i++) {
            source.onNext(i);
        }
        */

        TestSource source = new TestSource();

        Flowable.create(emitter -> {
            TestSource.TestListener listener = item -> {
                if (emitter.isCancelled()) return;

                emitter.onNext(item);
            };

            source.addListener(listener);

            emitter.setCancellable(() -> source.removeListeners());
        }, BackpressureStrategy.ERROR)
        .observeOn(Schedulers.computation())
        //.subscribe(ComputeFunction::compute);
        //.subscribe(ComputeFunction::compute, Throwable::printStackTrace, Functions.EMPTY_ACTION, s -> s.request(10));
        .subscribe(new TestSubscriber());

        source.start();
    }

    public static class TestSubscriber implements FlowableSubscriber<Object> {
        private Subscription s;

        @Override
        public void onNext(Object t) {
            ComputeFunction.compute(t);

            s.request(1);
        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onSubscribe(Subscription s) {
            this.s = s;
            
            s.request(1);
		}
    }

    public static class TestSource {
        private List<TestListener> listeners;

        public TestSource() { listeners = new ArrayList<>(); }

        public interface TestListener { void onItem(Object item); }

        public void addListener(TestListener listener) { listeners.add(listener); }

        public void removeListeners() { listeners.clear(); }

        public void start() { for (int nn = 0; nn < 200; ++nn) {

        } }

        private void apply(Object item) { listeners.forEach(listener -> listener.onItem(item)); }
    }
}