package com.pawelnu.projectmanager.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class RowPagination {
  private Long totalElements;
  private Integer totalPages;
}
