package com.pawelnu.BackendProjectManager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PageValues {
  PAGE_NUMBER(0),
  PAGE_SIZE(10);

  private final Integer value;
}
