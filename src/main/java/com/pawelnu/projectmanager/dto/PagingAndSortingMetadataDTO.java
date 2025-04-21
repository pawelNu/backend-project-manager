package com.pawelnu.projectmanager.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PagingAndSortingMetadataDTO {

  private Integer pageNumber;
  private Integer pageSize;
  private Integer totalPages;
  private Long totalElements;
  private Boolean first;
  private Boolean last;
  private String sortingField;
  private Boolean isAscendingSorting;
  private Boolean hasPrevious;
  private Boolean hasNext;
}
