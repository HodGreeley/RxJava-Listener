package com.couchbase.rx;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import java.util.stream.IntStream;

public class FunctionSource<T> extends BasicSource<T> {
  private Function<Integer, ? extends T> function;
  private OnItemListener<? super T> current;
  private int count;

  public static <U> Observable<U> fromFunction(Function<Integer, ? extends U> function, int count) {
    System.out.println("Creating source on thread " + Thread.currentThread().getName());

    FunctionSource<U> source = new FunctionSource<>();

    source.function = function;
    source.count = count;

    FunctionItemObservable<U> observable = new FunctionItemObservable<>(source);

    return observable;
  }

  public void start() {
    System.out.println("Source emitting on thread " + Thread.currentThread().getName());

    IntStream.range(0, count).forEach(nn -> {
      System.out.println("Emitting item #" + nn + " on thread " + Thread.currentThread().getName());

      current = listener;

      if (null == current) return;

      try {
        current.onItem(function.apply(nn));
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
  }
}