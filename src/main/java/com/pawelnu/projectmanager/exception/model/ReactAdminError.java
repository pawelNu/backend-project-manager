package com.pawelnu.projectmanager.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@AllArgsConstructor
public class ReactAdminError {
  private String message;
}
