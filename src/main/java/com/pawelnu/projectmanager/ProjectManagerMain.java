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

// TODO api for companies
// TODO api for company_addresses
// TODO api for employees
// TODO api for employee_authorities
// TODO api for projects
// TODO api for project_steps
// TODO api for project_step_comments
// TODO api for tickets
// TODO api for attachments
// TODO api for ticket_histories
