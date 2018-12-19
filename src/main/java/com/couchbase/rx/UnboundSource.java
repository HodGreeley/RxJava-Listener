// File: src/main/java/com/couchbase/rx/UnboundSource.java
package com.couchbase.rx;

import java.util.function.Supplier;

import static com.couchbase.Util.sleep;

public class UnboundSource<T> extends BasicSource<T> {
  private Supplier<T> supplier;
  private OnItemListener<? super T> current;

  public UnboundSource(Supplier<T> supplier) {
    this.supplier = supplier;
  }

  public void start() {
    System.out.println("Source emitting on thread " + Thread.currentThread().getName());

    for (;;) {
      System.out.println("Emitting item on thread " + Thread.currentThread().getName());
      
      current = listener;

      if (null != current) current.onItem(supplier.get());

      sleep(100);
    }
  }
}