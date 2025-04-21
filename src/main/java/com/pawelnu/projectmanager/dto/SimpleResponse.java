package com.pawelnu.projectmanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleResponse {
  private String message;
}
