package com.pawelnu.projectmanager.endpoints.category;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryEditRequestDTO {
  @NotNull private String name;
}
