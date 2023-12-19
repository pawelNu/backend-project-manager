package com.pawelnu.BackendProjectManager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagingAndSortingMetadataDTO {

    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalElements;
    private Boolean first;
    private Boolean last;
    private String sortingField;
    private String isAscendingSorting;
}
