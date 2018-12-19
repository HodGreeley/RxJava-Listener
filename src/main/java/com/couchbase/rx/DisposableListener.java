// File: src/main/java/com/couchbase/rx/DisposableListener.java
package com.couchbase.rx;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.disposables.Disposable;

public abstract class DisposableListener implements Disposable {
  private final AtomicBoolean unsubscribed = new AtomicBoolean();

  @Override
  public final boolean isDisposed() {
      return unsubscribed.get();
  }

  @Override
  public final void dispose() {
      if (unsubscribed.compareAndSet(false, true)) {        
        onDispose();
      }
  }

  protected abstract void onDispose();
}