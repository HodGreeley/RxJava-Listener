package com.couchbase.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

import com.couchbase.Util.ComputeFunction;
import static com.couchbase.Util.sleep;

public class Observables {
  private static Disposable d;
  private static DisposableObserver<Object> observer;

  public static void main(String[] args) {
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

    /*
    //-- Observer implementation block

    d = 
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

    d =
    source
    //.observeOn(Schedulers.computation())
    .subscribe(ComputeFunction::compute, Throwable::printStackTrace, () -> System.out.println("Done"), s -> s.request(10));

    for (int i = 0; i < 1_000_000; i++) {
        source.onNext(i);
    }

    // */

    /*
    Observable<Object> observable = FunctionSource.fromFunction(nn -> new Object(), 200);

    d = 
    observable
      .subscribeOn(Schedulers.io())
      .observeOn(Schedulers.computation())
      .subscribe(ComputeFunction::compute, Throwable::printStackTrace,
        () -> System.out.println("Done"), t -> System.out.println("onSubscribe on thread " + Thread.currentThread().getName()));

    //observable
    //  .subscribe(ComputeFunction::compute, Throwable::printStackTrace,
    //    () -> System.out.println("Done"), t -> System.out.println("onSubscribe on thread " + Thread.currentThread().getName()));
    
    // */

    ///*
    //SuppliedSource<Object> source = new SuppliedSource<>(Object::new);
    UnboundSource<Object> source = new UnboundSource<>(Object::new);

    ObservableOnSubscribe<Object> handler = emitter -> {
      System.out.println("Create on thread - " + Thread.currentThread().getName());

      source.setOnItemListener(item -> {
        System.out.println("Listen on thread - " + Thread.currentThread().getName());

        if (emitter.isDisposed()) return;
        emitter.onNext(item);
      });

      emitter.setCancellable(() -> source.setOnItemListener(null));
      //source.start(emitter);
      source.start();
    };

    d =
    Observable.create(handler)
      //.subscribeOn(Schedulers.io())
      .observeOn(Schedulers.computation())
      .subscribe(ComputeFunction::compute, Throwable::printStackTrace,
        () -> System.out.println("Done"), t -> System.out.println("onSubscribe on thread " + Thread.currentThread().getName()));
      //.subscribeWith(observer);

    // */

    sleep(3000);

    d.dispose();

    System.out.println("Main app waiting on thread - " + Thread.currentThread().getName());
    sleep(205000);
  }
}