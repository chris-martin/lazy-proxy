package org.codeswarm.providerproxy;

import javax.inject.Provider;

class MemoizingProvider<T> implements Provider<T> {

  private final Provider<? extends T> provider;

  private boolean gotten;
  private T t;

  public MemoizingProvider(
    Provider<? extends T> provider
  ) {
    this.provider = provider;
  }

  @Override
  public synchronized T get() {
    if (!gotten) {
      t = provider.get();
      gotten = true;
    }
    return t;
  }

}
