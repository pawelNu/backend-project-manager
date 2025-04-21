package com.pawelnu.projectmanager.dto;

import com.pawelnu.projectmanager.enums.PageValues;
import lombok.Data;

@Data
public class PagingAndSortingRequestDTO {

  private Integer pageNumber = PageValues.PAGE_NUMBER.getValue();
  private Integer pageSize = PageValues.PAGE_SIZE.getValue();
  private String sortedBy;
  private String direction;
}
