package com.pawelnu.projectmanager.endpoints.category;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDTO {

  private UUID id;
  private String name;
}
