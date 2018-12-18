package com.couchbase.rx;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import io.reactivex.ObservableEmitter;

public class SuppliedSource<T> extends BasicSource<T> {
  private Supplier<T> supplier;
  private OnItemListener<? super T> current;

  public SuppliedSource(Supplier<T> supplier) { this.supplier = supplier; }

  public void start(ObservableEmitter<? extends T> emitter) {
    System.out.println("Source emitting on thread " + Thread.currentThread().getName());

    IntStream.range(0, 200).forEach(nn -> {
      System.out.println("Emitting item #" + nn + " on thread " + Thread.currentThread().getName());
      
      current = listener;

      if (null != current) current.onItem(supplier.get());
    });

    if (emitter.isDisposed()) return;

    emitter.onComplete();
  }
}