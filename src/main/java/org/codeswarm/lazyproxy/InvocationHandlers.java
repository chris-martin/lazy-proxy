package org.codeswarm.lazyproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Methods for creating {@link InvocationHandler} instances.
 */
public final class InvocationHandlers {

  private InvocationHandlers() { }

  /**
   * An {@link InvocationHandler} that invokes on
   * an object returned from a {@link Callable}.
   * {@link Callable#call()} is invoked lazily
   * (the first time a method on the proxy is invoked).
   */
  public static <T> InvocationHandler forCallable(
      Callable<? extends T> callable) {

    return new CallableInvocationHandler<T>(callable);
  }

  static class CallableInvocationHandler<T>
      implements InvocationHandler {

    private final Callable<? extends T> callable;

    public CallableInvocationHandler(
        Callable<? extends T> callable) {

      this.callable = callable;
    }

    @Override
    public Object invoke(
      Object proxy, Method method, Object[] args
    ) throws Throwable {

      try {
        return method.invoke(callable.call(), args);
      } catch (InvocationTargetException e) {
        throw e.getTargetException();
      }
    }

  }

}
