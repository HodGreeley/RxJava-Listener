// File: src/main/java/com/couchbase/rx/Experiments.java
package com.couchbase.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

import com.couchbase.Util.ComputeFunction;
import static com.couchbase.Util.sleep;

public class Experiments {
  private static Observable<Object> observable;
  private static DisposableObserver<Object> observer;
  private static Disposable disposable;

  public static void main(String[] args) {
    /*
    //-- Observer implementation block

    observer = new DisposableObserver<Object>() {
      @Override
      public void onStart() {
        System.out.println("onSubscribe on thread " + Thread.currentThread().getName());
      }

      @Override
      public void onNext(Object t) { ComputeFunction.compute(t); }

      @Override
      public void onError(Throwable e) { e.printStackTrace(); }

      @Override
      public void onComplete() { System.out.println("Done"); }
    };

    //-- Observer implementation block
    // */

    /*
    //-- Hot vs. Cold Observable example block

    observable = Observable.fromCallable(() -> {
      System.out.println("fromCallable on thread " + Thread.currentThread().getName());
      return new Object();
    }); //.share();

    observable
      .subscribeOn(Schedulers.io())
      .subscribe(ComputeFunction::compute, Throwable::printStackTrace,
        () -> System.out.println("Done"), t -> System.out.println("onSubscribe on thread " + Thread.currentThread().getName()));
    
    observable
      .subscribe(ComputeFunction::compute, Throwable::printStackTrace,
        () -> System.out.println("Done"), t -> System.out.println("onSubscribe on thread " + Thread.currentThread().getName()));

    //-- Hot vs. Cold Observable example block
    // */

    /*
    disposable = 
    Observable
    .fromCallable(() -> {
      System.out.println("fromCallable on thread " + Thread.currentThread().getName());
      return new Object();
    })
    //.range(0, 200)
    .map(nn -> { 
      System.out.println("Processing on thread " + Thread.currentThread().getName());
      return nn;
    })
    .subscribeOn(Schedulers.io())
    //.observeOn(Schedulers.computation())
    .subscribe(ComputeFunction::compute, Throwable::printStackTrace,
      () -> System.out.println("Done"), t -> System.out.println("onSubscribe on thread " + Thread.currentThread().getName()));

    // */

    /*
    PublishProcessor<Integer> source = PublishProcessor.create();

    disposable =
    source
    //.observeOn(Schedulers.computation())
    .subscribe(ComputeFunction::compute, Throwable::printStackTrace, () -> System.out.println("Done"), s -> s.request(10));

    for (int i = 0; i < 1_000_000; i++) {
        source.onNext(i);
    }

    // */

    ///*
    //-- Custom Observable creation example block

    UnboundSource<Object> source = new UnboundSource<>(Object::new);
    //SuppliedSource<Object> source = new SuppliedSource<>(Object::new);

    ObservableOnSubscribe<Object> handler = emitter -> {
      System.out.println("Create on thread - " + Thread.currentThread().getName());

      source.setOnItemListener(item -> {
        System.out.println("Listen on thread - " + Thread.currentThread().getName());

        if (emitter.isDisposed()) return;
        emitter.onNext(item);
      });

      emitter.setCancellable(() -> source.setOnItemListener(null));
      source.start();
      //source.start(emitter);
    };
    // */

    //observable = FunctionSource.fromFunction(nn -> new Object(), 200);
    observable = Observable.create(handler); //.share();

    disposable =
    observable
      //.subscribeOn(Schedulers.io())
      //.observeOn(Schedulers.computation())
      .subscribe(ComputeFunction::compute, Throwable::printStackTrace,
        () -> System.out.println("Done"), t -> System.out.println("onSubscribe on thread " + Thread.currentThread().getName()));
      //.subscribeWith(observer);

    // */

    /*
    //Observable.create(handler)
    observable
      //.subscribeOn(Schedulers.io())
      //.observeOn(Schedulers.computation())
      .subscribe(ComputeFunction::compute, Throwable::printStackTrace,
        () -> System.out.println("Done"), t -> System.out.println("onSubscribe on thread " + Thread.currentThread().getName()));

    //-- Custom Observable creation example block
    // */

    /*
    sleep(3000);

    disposable.dispose();
    // */

    System.out.println("Main app waiting on thread - " + Thread.currentThread().getName());
    sleep(205000);
  }
}