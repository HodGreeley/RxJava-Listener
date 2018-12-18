package com.couchbase;

public class Util {
  public static class ComputeFunction {
    public static void compute(Object o) {
      System.out.println("Compute: object " + o.toString() + " on thread " + Thread.currentThread().getName());

      sleep(1000);
    }
  }

  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException ex) {
      System.out.println(ex.toString());
    }
  }
}