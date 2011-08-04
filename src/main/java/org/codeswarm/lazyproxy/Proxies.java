package org.codeswarm.lazyproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.Callable;

/**
 * Methods for creating instances via {@link Proxy}.
 */
public final class Proxies {

  private Proxies() { }

  /**
   * A convenient pass-through to
   * {@link Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)}
   * for proxying a single type.
   */
  @SuppressWarnings("unchecked")
  public static <T> T proxy(
      Class<T> type, InvocationHandler invocationHandler) {

    return (T) Proxy.newProxyInstance(
      type.getClassLoader(),
      new Class[] { type },
      invocationHandler
    );
  }

  /**
   * Returns a proxy that delegates to the result from a callable.
   *
   * {@link Callable#call()} is called at every invocation.
   * @param type type of returned proxy
   * @param callable callable whose result will be proxied
   * @return a proxy that delegates to the result from a callable
   */
  public static <T> T proxy(
      Class<T> type, Callable<? extends T> callable) {

    return proxy(
      type,
      InvocationHandlers.forCallable(callable)
    );
  }

  /**
   * <p>Returns a proxy that delegates all invocations to
   * an object retrieved from {@link Callable#call()},
   * which is called exactly once (the first time a method
   * is invoked on the proxy).</p>
   *
   * <p>This is useful for dealing with cyclic dependencies
   * between constructor-injected classes. For example:</p>
   *
   * <blockquote><pre>{@code

  class A {
    A(B b) { }
  }

  class B {
    B(A a) { }
  }

  A a() {
    return new A(b());
  }

  B b() {
    return Proxies.lazyProxy(A.class, new Callable<B>() {
      public B call() {
        return new B(a());
      }
    });
  }

}</pre></blockquote>
  *
  * @param type type of returned proxy
  * @param callable provider whose result to proxy
  * @return Returns a proxy that delegates all invocations
  *         to a {@code T} retrieved from {@link Callable#call()}.
  */
  public static <T> T lazyProxy(
      Class<T> type, Callable<? extends T> callable) {

    return proxy(
      type,
      Callables.memoize(callable)
    );
  }

}
