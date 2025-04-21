package com.pawelnu.projectmanager.utils;

import com.pawelnu.projectmanager.utils.Consts.Request;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Shared {

  public static Pageable preparePageable(
      Integer pageNumber, Integer pageSize, String sortedBy, String direction) {
    if (pageNumber == null) {
      pageNumber = Request.PAGE;
    }
    if (pageSize == null) {
      pageSize = Request.SIZE;
    }
    Sort sort = getSort(sortedBy, direction);
    return PageRequest.of(pageNumber, pageSize, sort);
  }

  private static Sort getSort(String sortedBy, String direction) {
    if (direction != null && sortedBy != null) {
      return switch (direction.toLowerCase()) {
        case "asc" -> Sort.by(sortedBy).ascending();
        case "desc" -> Sort.by(sortedBy).descending();
        default -> Sort.unsorted();
      };
    }
    return Sort.unsorted();
  }
}
