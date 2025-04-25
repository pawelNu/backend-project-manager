package com.pawelnu.projectmanager.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.utils.Consts.Request;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class Shared {

  public static Pageable preparePageable(
      Integer pageNumber, Integer pageSize, String sortedBy, String direction) {
    if (pageNumber == null) {
      pageNumber = Request.PAGE_NUMBER;
    }
    if (pageSize == null) {
      pageSize = Request.PAGE_SIZE_NUMBER;
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

  public static List<String> parseJsonList(ObjectMapper o, String json) {
    try {
      return o.readValue(json, new TypeReference<>() {});
    } catch (Exception e) {
      return List.of();
    }
  }

  public static List<Integer> parseJsonListInt(ObjectMapper o, String json) {
    try {
      return o.readValue(json, new TypeReference<>() {});
    } catch (Exception e) {
      return List.of(0, 24);
    }
  }

  public static Map<String, String> parseJsonMap(ObjectMapper o, String json) {
    try {
      return o.readValue(json, new TypeReference<>() {});
    } catch (Exception e) {
      return Map.of();
    }
  }
}
