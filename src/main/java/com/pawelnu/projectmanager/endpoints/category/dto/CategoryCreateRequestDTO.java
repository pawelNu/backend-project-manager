package com.pawelnu.projectmanager.endpoints.category.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryCreateRequestDTO {

  @NotNull
  @Size(min = 5, max = 255, message = "Name should has 5-255 characters")
  private String name;
}
