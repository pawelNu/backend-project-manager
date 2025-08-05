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
    public static final String AUTHORITY_NOT_FOUND_MSG = "Authority not found with id: ";
    public static final String AUTHORITIES_NOT_FOUND_MSG = "Authorities not found for ids: ";
    public static final String EMPLOYEE_AUTHORITIES_NOT_FOUND_MSG =
        "Employee authorities not found for ids: ";
    public static final String EMPLOYEE_AUTHORITY_NOT_FOUND_MSG =
        "Employee authority not found with id: ";
    public static final String CATEGORY_NOT_FOUND_MSG = "Category not found with id: ";
    public static final String CATEGORY_VALUE_NOT_FOUND_MSG = "Category value not found with id: ";
    public static final String PROJECT_NOT_FOUND = "Project not found with id: ";
    public static final String PROJECT_STEP_NOT_FOUND = "Project step not found with id: ";
    public static final String PROJECT_STEP_COMMENT_NOT_FOUND = "Project step comment not found with id: ";
  }
}
