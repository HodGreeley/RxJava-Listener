// File: src/main/java/com/couchbase/rx/FunctionItemObservable.java
package com.couchbase.rx;

import io.reactivex.Observable;
import io.reactivex.Observer;

import com.couchbase.rx.BasicSource.OnItemListener;

public class FunctionItemObservable<T> extends Observable<T> {
  private FunctionSource<T> source;

  public FunctionItemObservable(FunctionSource<T> source) { this.source = source; }

  @Override
  public void subscribeActual(Observer<? super T> observer) {
    Listener listener = new Listener(source, observer);
    source.setOnItemListener(listener);
    observer.onSubscribe(listener);
    source.start();

    if (!listener.isDisposed()) observer.onComplete();
  }

  private class Listener extends DisposableListener implements OnItemListener<T> {
    private FunctionSource<? extends T> source;
    private Observer<? super T> observer;

    Listener(FunctionSource<? extends T> source, Observer<? super T> observer) {
      this.source = source;
      this.observer = observer;
    }

    @Override
    public void onItem(T item) {
      if (!isDisposed()) {
        observer.onNext(item);
      }
    }

    @Override
    protected void onDispose() {
      source.setOnItemListener(null);
    }
  }
}