package com.pawelnu.projectmanager.endpoints.category.value;

import com.pawelnu.projectmanager.endpoints.category.CategoryDTO;
import com.pawelnu.projectmanager.endpoints.category.CategoryEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
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
    BooleanBuilder allConditions = new BooleanBuilder();

    if (filters.containsKey(categoryValue.name.getMetadata().getName())) {
      allConditions.and(
          categoryValue.name.likeIgnoreCase(
              "%" + filters.get(categoryValue.name.getMetadata().getName()) + "%"));
    }
    allConditions.and(categoryValue.isDeleted.isFalse());
    if (sortDir == null || sortDir.isEmpty()) {
      sortDir = "ASC";
    }

    JPAQuery<CategoryDTO> query =
        queryFactory
            .select(
                Projections.constructor(CategoryDTO.class, categoryValue.id, categoryValue.name))
            .from(categoryValue)
            .where(allConditions)
            .offset(offset)
            .limit(limit);

    if (sortField != null && !sortField.isEmpty()) {
      PathBuilder<CategoryEntity> entityPath =
          new PathBuilder<>(CategoryEntity.class, "authorityEntity");

      Order order = sortDir.equalsIgnoreCase("DESC") ? Order.DESC : Order.ASC;

      if (sortField.equals("name")) {
        query.orderBy(new OrderSpecifier<>(order, categoryValue.name));
      } else {
        query.orderBy(new OrderSpecifier<>(order, entityPath.getString(sortField)));
      }
    }

    List<CategoryDTO> results = query.fetch();
    long total =
        Optional.ofNullable(
                queryFactory
                    .select(categoryValue.count())
                    .from(categoryValue)
                    .where(allConditions)
                    .fetchOne())
            .orElse(0L);

    return new PageImpl<>(results, PageRequest.of(offset / limit, limit), total);
  }
}
