package com.pawelnu.projectmanager.enums;

import com.pawelnu.projectmanager.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
  NO("No"),
  YES("Yes");

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
