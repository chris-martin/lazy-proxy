package org.codeswarm.providerproxy;

import javax.inject.Provider;

public final class Providers {

  private Providers() { }

  /**
   * A {@link Provider} decorator that memoizes the result.
   *
   * @param provider provider to memoize
   */
  public static <T> Provider<T> memoize(
    Provider<T> provider
  ) {
    return new MemoizingProvider<T>(provider);
  }

}
