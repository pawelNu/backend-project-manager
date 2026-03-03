package com.pawelnu.projectmanager.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Path {

  private static final String API_PREFIX = "api";
  private static final String COMPANIES = "/companies";
  private static final String AUTH = "/auth";
  private static final String AUTHORITIES = "/authorities";
  private static final String CATEGORIES = "/categories";
  private static final String CATEGORY_VALUES = "/category-values";
  private static final String COMPANY_ADDRESSES = "/company-addresses";
  private static final String EMPLOYEES = "/employees";
  private static final String EMPLOYEE_AUTHORITIES = "/employee-authorities";
  private static final String PROJECTS = "/projects";
  private static final String PROJECT_STEP = "/project-steps";
  private static final String PROJECT_STEP_COMMENTS = "/project-step-comments";
  private static final String TICKETS = "/tickets";
  private static final String TICKET_HISTORIES = "/ticket-histories";

  public static final String API_AUTH = API_PREFIX + AUTH;
  public static final String API_AUTHORITIES = API_PREFIX + AUTHORITIES;
  public static final String API_CATEGORIES = API_PREFIX + CATEGORIES;
  public static final String API_CATEGORY_VALUES = API_PREFIX + CATEGORY_VALUES;
  public static final String API_COMPANIES = API_PREFIX + COMPANIES;
  public static final String API_COMPANY_ADDRESSES = API_PREFIX + COMPANY_ADDRESSES;
  public static final String API_EMPLOYEES = API_PREFIX + EMPLOYEES;
  public static final String API_EMPLOYEE_AUTHORITIES = API_PREFIX + EMPLOYEE_AUTHORITIES;
  public static final String API_PROJECTS = API_PREFIX + PROJECTS;
  public static final String API_PROJECT_STEPS = API_PREFIX + PROJECT_STEP;
  public static final String API_PROJECT_STEP_COMMENTS = API_PREFIX + PROJECT_STEP_COMMENTS;
  public static final String API_TICKETS = API_PREFIX + TICKETS;
  public static final String API_TICKET_HISTORIES = API_PREFIX + TICKET_HISTORIES;
}
