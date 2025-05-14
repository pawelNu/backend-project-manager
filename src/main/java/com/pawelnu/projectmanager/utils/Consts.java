package com.pawelnu.projectmanager.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Consts {
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Request {
    public static final Integer PAGE_NUMBER = 0;
    public static final String PAGE_NUMBER_STRING = "0";
    public static final Integer PAGE_SIZE_NUMBER = 20;
    public static final String PAGE_SIZE_NUMBER_STRING = "20";
    public static final String PAGE_NUMBER_NAME = "pageNumber";
    public static final String PAGE_SIZE_NAME = "pageSize";
    public static final String SORTED_BY_NAME = "sortedBy";
    public static final String DIRECTION_NAME = "direction";

    public static final String AUTH_HEADER = "authorization";
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class MSG {

    public static final String COMPANY_ADDRESS_NOT_FOUND = "Company address not found with id: ";
    public static final String COMPANY_NOT_FOUND = "Company not found with id: ";
    public static final String EMPLOYEE_NOT_FOUND = "Employee not found with id: ";
  }
}
