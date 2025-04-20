package com.pawelnu.projectmanager.mapper;

import com.pawelnu.projectmanager.dto.PagingAndSortingRequestDTO;
import com.pawelnu.projectmanager.enums.Messages;
import com.pawelnu.projectmanager.enums.PageValues;
import com.pawelnu.projectmanager.exception.NotFoundSortingFieldException;
import com.pawelnu.projectmanager.exception.NotNullOrEmptyException;
import com.pawelnu.projectmanager.listvalues.ListValues;
import com.pawelnu.projectmanager.model.PagingAndSortingMetadataDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

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

  default PagingAndSortingMetadataDTO toPagingAndSortingMetadataDTO(Page<?> page, Order sort) {
    //    return PagingAndSortingMetadataDTO.builder()
    //        .pageNumber(page.getNumber())
    //        .pageSize(page.getSize())
    //        .totalPages(page.getTotalPages())
    //        .totalElements(page.getTotalElements())
    //        .first(page.isFirst())
    //        .last(page.isLast())
    //        .sortingField(pagingAndSortingRequestDTO.getSortingField())
    //        .isAscendingSorting(pagingAndSortingRequestDTO.getIsAscendingSorting())
    //        .build();
    PagingAndSortingMetadataDTO paging = new PagingAndSortingMetadataDTO();
    paging.setPageNumber(page.getNumber());
    paging.setPageSize(page.getSize());
    paging.setTotalPages(page.getTotalPages());
    paging.setTotalElements(page.getTotalElements());
    paging.setFirst(page.isFirst());
    paging.setLast(page.isLast());
    paging.setHasPrevious(page.hasPrevious());
    paging.setHasNext(page.hasNext());
    paging.setSortingField(sort.getProperty());
    paging.setIsAscendingSorting(sort.isAscending());
    return paging;
  }
}
