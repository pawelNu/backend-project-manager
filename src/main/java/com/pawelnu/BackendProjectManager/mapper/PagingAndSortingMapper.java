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
                Boolean.TRUE.equals(pagingAndSortingRequestDTO.getAscendingFlag())
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        return PageRequest.of(
                pagingAndSortingRequestDTO.getPageNumber(),
                pagingAndSortingRequestDTO.getPageSize(),
                direction,
                pagingAndSortingRequestDTO.getField()
        );
    }

    default PagingAndSortingMetadataDTO toPagingAndSortingMetadataDTO(Page<?> page) {
        return PagingAndSortingMetadataDTO.builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .pagesCount(page.getTotalPages())
                .elementsCount(page.getTotalElements())
                .build();
    }

}
