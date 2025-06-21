package com.pawelnu.projectmanager.utils;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PageableResponse {
  private long totalElements;
  private int end;
}
