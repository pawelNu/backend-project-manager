package com.pawelnu.projectmanager.exception.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SimpleResponse {
  private String message;
}
