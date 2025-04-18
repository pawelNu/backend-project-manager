package com.pawelnu.BackendProjectManager.exception.model;

import lombok.Getter;

@Getter
public class InternalServerErrorModel {
  private String title;
  private Integer status;
  private String detail;
}
