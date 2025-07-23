package com.pawelnu.projectmanager.endpoints.category.dto;

import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueDTO;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CategoryDTO {

  private UUID id;
  private String name;
  private List<CategoryValueDTO> values;
}
