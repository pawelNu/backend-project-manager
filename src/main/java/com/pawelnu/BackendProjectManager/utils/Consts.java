package com.pawelnu.BackendProjectManager.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Consts {
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Request {
    public static final Integer PAGE = 1;
    public static final Integer SIZE = 20;

    public static final String AUTH_HEADER = "authorization";
  }
}
