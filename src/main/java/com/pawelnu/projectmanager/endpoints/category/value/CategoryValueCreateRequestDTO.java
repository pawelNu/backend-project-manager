package com.pawelnu.projectmanager.endpoints.category.value;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryValueCreateRequestDTO {
  private UUID categoryId;
  private BigDecimal numericValue;
  private String stringValue;
  private Instant dateValue;
}
