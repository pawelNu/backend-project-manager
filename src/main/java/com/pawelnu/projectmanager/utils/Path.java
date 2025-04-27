package com.pawelnu.projectmanager.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Path {
  private static final String API_PREFIX = "api";
  private static final String COMPANIES = "/companies";
  private static final String COMPANY_ADDRESSES = "/company-addresses";

  public static final String API_COMPANIES = API_PREFIX + COMPANIES;
  public static final String API_COMPANY_ADDRESSES = API_PREFIX + COMPANY_ADDRESSES;
}
