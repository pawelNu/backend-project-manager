package com.pawelnu.projectmanager.config.security.jwt;

import java.util.HashMap;
import java.util.Map;

public class AuthorityUtil {

  private static final Map<String, String> IRREGULAR_PLURALS = new HashMap<>();

  static {
    //    IRREGULAR_PLURALS.put("Company", "Companies");
    //    IRREGULAR_PLURALS.put("Category", "Categories");
  }

  //  public static String prepareAuthorityString(String className, String methodName) {
  //    String baseName = className.replace("Controller", "");
  //    return toUpperSnakeCase(baseName) + "_" + toUpperSnakeCase(methodName);
  //  }

  private static String toUpperSnakeCase(String input) {
    return input.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
  }

  public static String prepareAuthorityString(String className, String methodName) {
    String baseName = className.replace("Controller", "");
    String pluralBaseName = toPlural(baseName);
    return toUpperSnakeCase(pluralBaseName) + "_" + toUpperSnakeCase(methodName);
  }

  private static String toPlural(String singular) {
    if (IRREGULAR_PLURALS.containsKey(singular)) {
      return IRREGULAR_PLURALS.get(singular);
    }

    if (singular.endsWith("y")
        && singular.length() > 1
        && !isVowel(singular.charAt(singular.length() - 2))) {
      return singular.substring(0, singular.length() - 1) + "ies";
    } else if (singular.endsWith("s")
        || singular.endsWith("x")
        || singular.endsWith("z")
        || singular.endsWith("ch")
        || singular.endsWith("sh")) {
      return singular + "es";
    } else {
      return singular + "s";
    }
  }

  private static boolean isVowel(char c) {
    return "aeiouAEIOU".indexOf(c) != -1;
  }
}
