// File: src/main/java/com/couchbase/rx/Observables.java
package com.couchbase.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import com.couchbase.Util.ComputeFunction;

public class Observables {
  public static void main(String[] args) {
    UnboundSource<Object> source = new UnboundSource<>(Object::new);

    ObservableOnSubscribe<Object> handler = emitter -> {
      System.out.println("Create on thread - " + Thread.currentThread().getName());

      source.setOnItemListener(item -> {
        System.out.println("Listen on thread - " + Thread.currentThread().getName());

        if (emitter.isDisposed()) return;
        emitter.onNext(item);
      });

      emitter.setCancellable(() -> source.setOnItemListener(null));
      source.start();
    };

    Observable.create(handler)
      .subscribe(ComputeFunction::compute, Throwable::printStackTrace,
        () -> System.out.println("Done"), t -> System.out.println("onSubscribe on thread " + Thread.currentThread().getName()));
  }
}