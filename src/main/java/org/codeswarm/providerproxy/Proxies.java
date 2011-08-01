package org.codeswarm.providerproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import javax.inject.Provider;

/**
 * Methods for creating {@link Proxy} instances.
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
    Class<T> type,
    InvocationHandler invocationHandler
  ) {
    return (T) Proxy.newProxyInstance(
      type.getClassLoader(),
      new Class[] { type },
      invocationHandler
    );
  }

  /**
   * Returns a proxy that delegates to the result from a provider.
   *
   * {@link Provider#get()} is called at every invocation.
   * @param type type of returned proxy
   * @param provider provider whose result to proxy
   * @return a proxy that delegates to the result from a provider
   */
  public static <T> T providerProxy(
    Class<T> type,
    Provider<? extends T> provider
  ) {
    return proxy(
      type,
      InvocationHandlers.forProvider(provider)
    );
  }

/**
<p>Returns a proxy that delegates all invocations to an retrieved
from {@link Provider#get()}, which is called exactly once
(the first time a method is invoked on the proxy).</p>

<p>This is useful for dealing with cyclic dependencies between
constructor-injected classes. For example:</p>

<blockquote><pre>{@code

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
    return Proxies.memoProviderProxy(A.class, new Provider<B>() {
      public B get() {
        return new B(a());
      }
    });
  }

}</pre></blockquote>

@param type type of returned proxy
@param provider provider whose result to proxy
@return Returns a proxy that delegates all invocations
        to a {@code T} retrieved from {@link Provider#get()}.
*/
  public static <T> T memoizedProviderProxy(
    Class<T> type,
    Provider<? extends T> provider
  ) {
    return providerProxy(
      type,
      Providers.memoize(provider)
    );
  }

}
