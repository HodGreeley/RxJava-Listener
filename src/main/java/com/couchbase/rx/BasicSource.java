package com.couchbase.rx;

public class BasicSource<T> {
  protected volatile OnItemListener<? super T> listener;

  public static interface OnItemListener<T> { void onItem(T item); }

  public void setOnItemListener(OnItemListener<? super T> listener) { this.listener = listener; }
}