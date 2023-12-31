package com.pawelnu.BackendProjectManager.dto;

import com.pawelnu.BackendProjectManager.enums.PageValues;
import lombok.Data;

@Data
public class PagingAndSortingRequestDTO {

    private Integer pageNumber = PageValues.PAGE_NUMBER.getValue();
    private Integer pageSize = PageValues.PAGE_SIZE.getValue();
    private String sortingField;
    private Boolean isAscendingSorting;
}
