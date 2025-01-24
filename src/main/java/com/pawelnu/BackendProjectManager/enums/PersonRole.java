package com.pawelnu.BackendProjectManager.enums;

import com.pawelnu.BackendProjectManager.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PersonRole {
  CLIENT("Client"),
  CLIENT_LEADER("Client leader"),
  EMPLOYEE("Employee");

  private final String value;

  public static PersonRole fromValue(String value) {
    for (PersonRole projectStatus : values()) {
      if (projectStatus.value.equalsIgnoreCase(value)) {
        return projectStatus;
      }
    }
    throw new BadRequestException("Unsupported project status: " + value);
  }
}
