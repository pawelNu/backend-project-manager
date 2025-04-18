package com.pawelnu.BackendProjectManager.mapper;

import com.pawelnu.BackendProjectManager.dto.PagingAndSortingMetadataDTO;
import com.pawelnu.BackendProjectManager.dto.PagingAndSortingRequestDTO;
import com.pawelnu.BackendProjectManager.enums.Messages;
import com.pawelnu.BackendProjectManager.enums.PageValues;
import com.pawelnu.BackendProjectManager.exception.NotFoundSortingFieldException;
import com.pawelnu.BackendProjectManager.exception.NotNullOrEmptyException;
import com.pawelnu.BackendProjectManager.listvalues.ListValues;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Mapper(componentModel = "spring")
public interface PagingAndSortingMapper {

  default Pageable toPageable(PagingAndSortingRequestDTO pagingAndSortingRequestDTO) {

    String sortingField = pagingAndSortingRequestDTO.getSortingField();

    if (sortingField == null || sortingField.isEmpty()) {
      throw new NotNullOrEmptyException(Messages.SORTING_FIELD_CANNOT_BE_NULL_OR_EMPTY.getMsg());
    }

    if (!ListValues.projectSortingFields().contains(sortingField)) {
      throw new NotFoundSortingFieldException(
          Messages.NOT_FOUND_SORTING_FIELD.getMsg() + sortingField);
    }

    if (pagingAndSortingRequestDTO.getPageNumber() == null
        || pagingAndSortingRequestDTO.getPageNumber() < 0) {
      pagingAndSortingRequestDTO.setPageNumber(PageValues.PAGE_NUMBER.getValue());
    }

    if (pagingAndSortingRequestDTO.getPageSize() == null
        || pagingAndSortingRequestDTO.getPageSize() < 1) {
      pagingAndSortingRequestDTO.setPageSize(PageValues.PAGE_SIZE.getValue());
    }

    Sort.Direction direction =
        Boolean.TRUE.equals(pagingAndSortingRequestDTO.getIsAscendingSorting())
            ? Sort.Direction.ASC
            : Sort.Direction.DESC;

    return PageRequest.of(
        (pagingAndSortingRequestDTO.getPageNumber()),
        pagingAndSortingRequestDTO.getPageSize(),
        direction,
        pagingAndSortingRequestDTO.getSortingField());
  }

  default PagingAndSortingMetadataDTO toPagingAndSortingMetadataDTO(
      Page<?> page, PagingAndSortingRequestDTO pagingAndSortingRequestDTO) {
    return PagingAndSortingMetadataDTO.builder()
        .pageNumber(page.getNumber())
        .pageSize(page.getSize())
        .totalPages(page.getTotalPages())
        .totalElements(page.getTotalElements())
        .first(page.isFirst())
        .last(page.isLast())
        .sortingField(pagingAndSortingRequestDTO.getSortingField())
        .isAscendingSorting(pagingAndSortingRequestDTO.getIsAscendingSorting())
        .build();
  }
}
