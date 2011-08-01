package org.codeswarm.providerproxy;

import javax.inject.Provider;
import java.lang.reflect.InvocationHandler;

public final class InvocationHandlers {

  private InvocationHandlers() { }

  /**
   * An {@link InvocationHandler} that invokes on object returned
   * from a {@link Provider}. {@link Provider#get()} is invoked
   * lazily (only when/if a method on the proxy is invoked).
   */
  public static <T> InvocationHandler forProvider(
    Provider<? extends T> provider
  ) {
    return new ProviderInvocationHandler<T>(provider);
  }

}
