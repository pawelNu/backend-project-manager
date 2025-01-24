package com.pawelnu.BackendProjectManager.enums;

import com.pawelnu.BackendProjectManager.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
  MAINTAINED("Maintained"),
  CLOSED("Closed");

  private final String value;

  public static Status fromValue(String value) {
    for (Status status : values()) {
      if (status.value.equalsIgnoreCase(value)) {
        return status;
      }
    }
    throw new BadRequestException("Unsupported project status: " + value);
  }
}
