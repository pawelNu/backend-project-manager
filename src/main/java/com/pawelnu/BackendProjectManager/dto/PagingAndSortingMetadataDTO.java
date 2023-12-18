package com.pawelnu.BackendProjectManager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagingAndSortingMetadataDTO {

    private Integer pageNumber;

    private Integer pageSize;

    private Integer pagesCount; // TODO rename to totalPages

    private Long elementsCount; // TODO rename to totalElements

//    TODO add page elements:
//     last
//     first
//     sort field
//     sort direction

}
