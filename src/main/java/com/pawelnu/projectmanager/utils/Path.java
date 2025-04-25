package com.pawelnu.projectmanager.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Path {
  private static final String API_PREFIX = "api";
  private static final String COMPANIES = "/companies";

  public static final String API_COMPANIES = API_PREFIX + COMPANIES;
}
