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

    private static final String NOT_FOUND = " not found with id: ";
    public static final String COMPANY_ADDRESS_NOT_FOUND = "Company address" + NOT_FOUND;
    public static final String COMPANY_NOT_FOUND = "Company" + NOT_FOUND;
    public static final String EMPLOYEE_NOT_FOUND = "Employee" + NOT_FOUND;
    public static final String AUTHORITY_NOT_FOUND_MSG = "Authority" + NOT_FOUND;
    public static final String AUTHORITIES_NOT_FOUND_MSG = "Authorities not found for ids: ";
    public static final String EMPLOYEE_AUTHORITIES_NOT_FOUND_MSG =
        "Employee authorities not found for ids: ";
    public static final String EMPLOYEE_AUTHORITY_NOT_FOUND_MSG = "Employee authority" + NOT_FOUND;
    public static final String CATEGORY_NOT_FOUND_MSG = "Category" + NOT_FOUND;
    public static final String CATEGORY_VALUE_NOT_FOUND_MSG = "Category value" + NOT_FOUND;
    public static final String PROJECT_NOT_FOUND = "Project" + NOT_FOUND;
    public static final String PROJECT_STEP_NOT_FOUND = "Project step" + NOT_FOUND;
    public static final String PROJECT_STEP_COMMENT_NOT_FOUND = "Project step comment" + NOT_FOUND;
    public static final String TICKET_NOT_FOUND = "Ticket" + NOT_FOUND;
    public static final String TICKET_HISTORY_NOT_FOUND = "Ticket history" + NOT_FOUND;
  }
}
