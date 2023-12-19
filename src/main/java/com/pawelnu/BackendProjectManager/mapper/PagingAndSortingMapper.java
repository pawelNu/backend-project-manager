package com.pawelnu.BackendProjectManager.mapper;

import com.pawelnu.BackendProjectManager.dto.PagingAndSortingMetadataDTO;
import com.pawelnu.BackendProjectManager.dto.PagingAndSortingRequestDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Mapper(componentModel = "spring")
public interface PagingAndSortingMapper {

    default Pageable toPageable(PagingAndSortingRequestDTO pagingAndSortingRequestDTO) {

        Sort.Direction direction =
                Boolean.TRUE.equals(pagingAndSortingRequestDTO.getIsAscendingSorting())
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        return PageRequest.of(
                pagingAndSortingRequestDTO.getPageNumber(),
                pagingAndSortingRequestDTO.getPageSize(),
                direction,
                pagingAndSortingRequestDTO.getSortingField());
    }

    // TODO how to transfer field and direction sorting from PagingAndSortingRequestDTO
    //  to PagingAndSortingMetadataDTO
    //  private String sortingField;
    //  private String isAscendingSorting;

    default PagingAndSortingMetadataDTO toPagingAndSortingMetadataDTO(Page<?> page) {
        return PagingAndSortingMetadataDTO.builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
