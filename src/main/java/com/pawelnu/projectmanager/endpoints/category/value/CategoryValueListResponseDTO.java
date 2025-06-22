package com.pawelnu.projectmanager.endpoints.category.value;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryValueListResponseDTO {
  private List<CategoryValueDTO> data;
  private String contentRange;
}
