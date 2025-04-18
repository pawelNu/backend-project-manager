package com.pawelnu.BackendProjectManager.enums;

import com.pawelnu.BackendProjectManager.exception.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyStatus {
  ACTIVE("Active"),
  TERMINATED("Terminated");

  private final String value;

  public static CompanyStatus fromValue(String value) {
    for (CompanyStatus companyStatus : values()) {
      if (companyStatus.value.equalsIgnoreCase(value)) {
        return companyStatus;
      }
    }
    throw new BadRequestException("Unsupported company status: " + value);
  }
}
