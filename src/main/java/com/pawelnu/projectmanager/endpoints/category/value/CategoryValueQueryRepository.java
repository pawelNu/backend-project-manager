package com.pawelnu.projectmanager.endpoints.category.value;

import com.pawelnu.projectmanager.endpoints.category.QCategoryEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CategoryValueQueryRepository {
  private final JPAQueryFactory queryFactory;

  public Page<CategoryValueDTO> filter(
      Map<String, String> filters, int offset, int limit, String sortDir, String sortField) {
    QCategoryValueEntity categoryValue = QCategoryValueEntity.categoryValueEntity;
    QCategoryEntity category = QCategoryEntity.categoryEntity;
    BooleanBuilder allConditions = new BooleanBuilder();

    if (filters.containsKey(category.name.getMetadata().getName())) {
      allConditions.and(
          category.name.likeIgnoreCase(
              "%" + filters.get(category.name.getMetadata().getName()) + "%"));
    }
    allConditions.and(categoryValue.isDeleted.isFalse());
    if (sortDir == null || sortDir.isEmpty()) {
      sortDir = "ASC";
    }

    JPAQuery<CategoryValueDTO> query =
        queryFactory
            .select(
                Projections.constructor(
                    CategoryValueDTO.class,
                    categoryValue.id,
                    category.name,
                    categoryValue.numericValue,
                    categoryValue.stringValue,
                    categoryValue.dateValue))
            .from(categoryValue)
            .leftJoin(categoryValue.category, category)
            .where(allConditions)
            .offset(offset)
            .limit(limit);

    if (sortField != null && !sortField.isEmpty()) {
      Order order = sortDir.equalsIgnoreCase("DESC") ? Order.DESC : Order.ASC;

      switch (sortField) {
        case "name" -> query.orderBy(new OrderSpecifier<>(order, category.name));
        case "numericValue" -> query.orderBy(
            new OrderSpecifier<>(order, categoryValue.numericValue));
        case "stringValue" -> query.orderBy(new OrderSpecifier<>(order, categoryValue.stringValue));
        case "dateValue" -> query.orderBy(new OrderSpecifier<>(order, categoryValue.dateValue));
        default -> query.orderBy(new OrderSpecifier<>(order, categoryValue.id));
      }
    }

    List<CategoryValueDTO> results = query.fetch();
    long total =
        Optional.ofNullable(
                queryFactory
                    .select(categoryValue.count())
                    .from(categoryValue)
                    .leftJoin(categoryValue.category, category)
                    .where(allConditions)
                    .fetchOne())
            .orElse(0L);

    return new PageImpl<>(results, PageRequest.of(offset / limit, limit), total);
  }
}
