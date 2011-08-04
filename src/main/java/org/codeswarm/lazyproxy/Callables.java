package org.codeswarm.lazyproxy;

import java.util.concurrent.Callable;

/**
 * Methods for creating {@link Callable} instances.
 */
public final class Callables {

  private Callables() { }

  /**
   * A {@link Callable} decorator that memoizes its result.
   *
   * @param callable callable that shall be invoked
   *  by the decorator at most one time.
   */
  public static <T> Callable<T> memoize(
      Callable<? extends T> callable) {

    return new MemoizingCallable<T>(callable);
  }

  static class MemoizingCallable<T>
      implements Callable<T> {

    private final Callable<? extends T> callable;

    private boolean gotten;
    private T t;

    public MemoizingCallable(
        Callable<? extends T> callable) {

      this.callable = callable;
    }

    @Override
    public synchronized T call() throws Exception {
      if (!gotten) {
        t = callable.call();
        gotten = true;
      }
      return t;
    }

  }

}
