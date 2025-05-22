package com.pawelnu.projectmanager.endpoints.category.value;

import com.pawelnu.projectmanager.endpoints.category.CategoryEntity;
import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category_values")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryValueEntity extends Auditable {

  @Id @GeneratedValue private UUID id;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private CategoryEntity category;

  private BigDecimal numericValue;
  private String stringValue;
  private Instant dateValue;
}
