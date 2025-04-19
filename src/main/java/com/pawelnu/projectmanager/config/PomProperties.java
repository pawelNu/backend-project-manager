package com.pawelnu.projectmanager.config;

import com.pawelnu.projectmanager.ProjectManagerMain;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class PomProperties {
  public static Properties getPropertiesFromPOM() {
    Properties pom = new Properties();
    try {
      pom.load(new ClassPathResource("properties-from-pom.properties").getInputStream());
    } catch (IOException e) {
      LoggerFactory.getLogger(ProjectManagerMain.class).warn("POM properties exception");
    }
    return pom;
  }

  public static String getRelease() {
    return getPropertiesFromPOM().getProperty("api.version.release");
  }

  public static String getReleaseWithDesc() {
    return "release " + getRelease();
  }

  public static String getVersion() {
    return getPropertiesFromPOM().getProperty("api.version.number");
  }

  public static void logAppVersion() {
    String version = "Version: " + getVersion() + " Release: " + getRelease();
    LoggerFactory.getLogger(ProjectManagerMain.class).info(version);
  }

  public static void setLocaleProperties() {
    Locale.setDefault(new Locale("pl", "PL"));
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));
  }
}
