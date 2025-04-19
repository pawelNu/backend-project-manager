package com.pawelnu.projectmanager.exception.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class UnauthorizedModel {
  private final String error = "unauthorized";
  private final String error_description =
      "Full authentication is required to access this resource";

  @Schema(example = "unauthorized")
  public String getError() {
    return error;
  }

  @Schema(example = "Full authentication is required to access this resource")
  public String getError_description() {
    return error_description;
  }
}
