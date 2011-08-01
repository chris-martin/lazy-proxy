package org.codeswarm.providerproxy;

import javax.inject.Provider;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ProviderInvocationHandler<T>
    implements InvocationHandler {

  private final Provider<? extends T> provider;

  public ProviderInvocationHandler(
    Provider<? extends T> provider
  ) {
    this.provider = provider;
  }

  @Override
  public Object invoke(
    Object proxy,
    Method method,
    Object[] args
  ) throws Throwable {
    try {
      return method.invoke(provider.get(), args);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw e.getTargetException();
    }
  }

}
