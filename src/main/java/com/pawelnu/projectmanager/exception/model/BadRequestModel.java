package com.pawelnu.projectmanager.exception.model;

import java.util.List;
import lombok.Getter;
import org.zalando.problem.violations.Violation;

@Getter
public class BadRequestModel {
  private String title;
  private Integer status;
  private String detail;
  private List<Violation> violations;
}
