package com.pawelnu.projectmanager.exception.model;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BadRequestModel {

  private Map<String, Object> errors;
}
