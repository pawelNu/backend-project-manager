package com.pawelnu.BackendProjectManager;

import com.pawelnu.BackendProjectManager.config.PomProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectManagerMain {

  public static void main(String[] args) {
    PomProperties.setLocaleProperties();
    SpringApplication.run(ProjectManagerMain.class, args);
    PomProperties.logAppVersion();
  }
}
