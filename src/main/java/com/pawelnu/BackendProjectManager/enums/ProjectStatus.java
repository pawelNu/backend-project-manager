package com.pawelnu.BackendProjectManager.enums;

import com.pawelnu.BackendProjectManager.exception.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectStatus {
  MAINTAINED("Maintained"),
  CLOSED("Closed");

  private final String value;

  public static ProjectStatus fromValue(String value) {
    for (ProjectStatus projectStatus : values()) {
      if (projectStatus.value.equalsIgnoreCase(value)) {
        return projectStatus;
      }
    }
    throw new BadRequestException("Unsupported project status: " + value);
  }
}
