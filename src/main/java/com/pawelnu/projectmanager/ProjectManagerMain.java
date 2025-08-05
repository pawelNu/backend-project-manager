package com.pawelnu.projectmanager;

import com.pawelnu.projectmanager.config.PomProperties;
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

// TODO api for project_step_comments
// TODO api for tickets
// TODO api for attachments
// TODO api for ticket_histories
