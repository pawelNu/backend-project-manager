package com.pawelnu.projectmanager.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.utils.Consts.Request;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

  private static List<String> parseJsonList(ObjectMapper o, String json) {
    try {
      List<String> result = o.readValue(json, new TypeReference<>() {});
      if (result.getLast().equalsIgnoreCase("asc") || result.getLast().equalsIgnoreCase("desc")) {
        return result;
      } else {
        return List.of();
      }
    } catch (Exception e) {
      return List.of();
    }
  }

  private static List<Integer> parseJsonListInt(ObjectMapper o, String json) {
    try {
      return o.readValue(json, new TypeReference<>() {});
    } catch (Exception e) {
      return List.of(0, 24);
    }
  }

  private static Map<String, String> parseJsonMap(ObjectMapper o, String json) {
    try {
      return o.readValue(json, new TypeReference<>() {});
    } catch (Exception e) {
      return Map.of();
    }
  }

  public static String prepareContentRange(Page page, int offset, int limit) {
    // TODO refactor code and remove this function
    PageableResponse pageInfo = preparePageInfo(page, offset, limit);
    return String.format("items %d-%d/%d", offset, pageInfo.getEnd(), pageInfo.getTotalElements());
  }

  public static String prepareContentRange(Long totalElements, int offset, int limit) {
    PageableResponse pageInfo = preparePageInfo(totalElements, offset, limit);
    return String.format("items %d-%d/%d", offset, pageInfo.getEnd(), pageInfo.getTotalElements());
  }

  public static PageableParams preparePageableParams(
      ObjectMapper objectMapper, String sort, String range, String filter) {
    List<String> sortList = Shared.parseJsonList(objectMapper, sort);
    String sortField = sortList.isEmpty() ? "" : sortList.get(0);
    String sortDir = sortList.size() > 1 ? sortList.get(1) : "ASC";

    List<Integer> rangeList = Shared.parseJsonListInt(objectMapper, range);
    int offset = !rangeList.isEmpty() ? rangeList.get(0) : 0;
    int limit = rangeList.size() > 1 ? rangeList.get(1) - rangeList.get(0) + 1 : 25;

    Map<String, String> filters = Shared.parseJsonMap(objectMapper, filter);
    return PageableParams.builder()
        .sortField(sortField)
        .sortDir(sortDir)
        .offset(offset)
        .limit(limit)
        .filters(filters)
        .build();
  }

  //  TODO implement checking query params if they exist
  /*
  all params will be stored in map <item, keyParam>
  in service will be check
  checkIfParamsExists(params) {
  get only params for item
  loop for each param
  throw NotFoundQueryFieldException if not found with list all available params
  }
  */
  // TODO think for the same for sorting params

  private static PageableResponse preparePageInfo(Page page, int offset, int limit) {
    long totalElements = page.getTotalElements();
    long end = Math.min(offset + limit - 1, totalElements - 1);
    return PageableResponse.builder().totalElements(totalElements).end(end).build();
  }

  private static PageableResponse preparePageInfo(Long totalElements, int offset, int limit) {
    long end = Math.min(offset + limit - 1, totalElements - 1);
    return PageableResponse.builder().totalElements(totalElements).end(end).build();
  }

  public static String deleteMessage(String item, UUID id) {
    return "Deleted %s with id: %s".formatted(item, id);
  }

  public static String cannotDeleteMessage(String item, UUID id) {
    return "Cannot delete %s with id: %s".formatted(item, id);
  }

  public static NumberExpression<Long> totalElements() {
    return Expressions.numberTemplate(Long.class, "COUNT(*) OVER()").as("total_elements");
  }

  public static NumberExpression<Integer> totalPages(int limit) {
    return Expressions.numberTemplate(Integer.class, "CEIL(COUNT(*) OVER() * 1.0 / {0})", limit)
        .as("total_pages");
  }

  public static Instant parseDate(String stringDate) {
    LocalDate startDate = LocalDate.parse(stringDate);
    return startDate.atStartOfDay(ZoneOffset.UTC).toInstant();
  }

  public static class Field {
    public static final String name = "name";
    public static final String projectName = "projectName";
    public static final String projectStepName = "name";
    public static final String companyName = "companyName";
    public static final String assignedEmployee = "assignedEmployee";
    public static final String projectPriorityValue = "projectPriorityValue";
    public static final String projectStepPriorityValue = "projectStepPriorityValue";
    public static final String priorityValue = "priorityValue";
    public static final String deadline = "deadline";
  }
}
