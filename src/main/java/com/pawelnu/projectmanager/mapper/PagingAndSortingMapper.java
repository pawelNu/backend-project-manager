package com.pawelnu.projectmanager.mapper;

import com.pawelnu.projectmanager.dto.PagingAndSortingMetadataDTO;
import com.pawelnu.projectmanager.dto.PagingAndSortingRequestDTO;
import com.pawelnu.projectmanager.enums.Messages;
import com.pawelnu.projectmanager.enums.PageValues;
import com.pawelnu.projectmanager.exception.NotFoundSortingFieldException;
import com.pawelnu.projectmanager.listvalues.ListValues;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@Mapper(componentModel = "spring")
public interface PagingAndSortingMapper {

  default Pageable toPageable(PagingAndSortingRequestDTO page) {

    String sortingField = page.getSortedBy();

//    if (sortingField == null || sortingField.isEmpty()) {
//      throw new NotNullOrEmptyException(Messages.SORTING_FIELD_CANNOT_BE_NULL_OR_EMPTY.getMsg());
//    }

    if (!ListValues.projectSortingFields().contains(sortingField)) {
      throw new NotFoundSortingFieldException(
          Messages.NOT_FOUND_SORTING_FIELD.getMsg() + sortingField);
    }

    if (page.getPageNumber() == null
        || page.getPageNumber() < 0) {
      page.setPageNumber(PageValues.PAGE_NUMBER.getValue());
    }

    if (page.getPageSize() == null
        || page.getPageSize() < 1) {
      page.setPageSize(PageValues.PAGE_SIZE.getValue());
    }

    if (page.getDirection() == null) {
      return PageRequest.of(
          page.getPageNumber(),
          page.getPageSize());
    } else {
      Sort.Direction direction =
          "asc".equals(page.getDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC;
      return PageRequest.of(
          (page.getPageNumber()),
          page.getPageSize(),
          direction,
          page.getSortedBy());
    }
  }

  default PagingAndSortingMetadataDTO toPagingAndSortingMetadataDTO(Page<?> page, Order sort) {
    PagingAndSortingMetadataDTO paging = new PagingAndSortingMetadataDTO();
    paging.setPageNumber(page.getNumber());
    paging.setPageSize(page.getSize());
    paging.setTotalPages(page.getTotalPages());
    paging.setTotalElements(page.getTotalElements());
    paging.setFirst(page.isFirst());
    paging.setLast(page.isLast());
    paging.setHasPrevious(page.hasPrevious());
    paging.setHasNext(page.hasNext());
    if (sort == null) {
      paging.setSortingField(null);
      paging.setIsAscendingSorting(null);
    } else {
      paging.setSortingField(sort.getProperty());
      paging.setIsAscendingSorting(sort.isAscending());
    }
    return paging;
  }
}
