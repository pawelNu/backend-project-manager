package com.pawelnu.projectmanager.endpoints.category.value;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryValueDTO {
  private UUID id;
  private String categoryName;
  private BigDecimal numericValue;
  private String stringValue;
  private Instant dateValue;
}
