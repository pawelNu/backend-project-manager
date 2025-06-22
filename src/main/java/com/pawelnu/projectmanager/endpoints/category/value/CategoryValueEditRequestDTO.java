package com.pawelnu.projectmanager.endpoints.category.value;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryValueEditRequestDTO {
  private BigDecimal numericValue;
  private String stringValue;
  private Instant dateValue;
}
