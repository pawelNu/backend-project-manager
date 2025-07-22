package com.pawelnu.projectmanager.endpoints.category;

import com.pawelnu.projectmanager.endpoints.category.dto.CategoryDTO;
import com.pawelnu.projectmanager.endpoints.category.value.QCategoryValueEntity;
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
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CategoryQueryRepository {

  private final JPAQueryFactory queryFactory;

  public Page<CategoryDTO> filter(
      Map<String, String> filters, int offset, int limit, String sortDir, String sortField) {
    QCategoryEntity category = QCategoryEntity.categoryEntity;
    BooleanBuilder allConditions = new BooleanBuilder();

    if (filters.containsKey(category.name.getMetadata().getName())) {
      allConditions.and(
          category.name.likeIgnoreCase(
              "%" + filters.get(category.name.getMetadata().getName()) + "%"));
    }
    allConditions.and(category.isDeleted.isFalse());
    if (sortDir == null || sortDir.isEmpty()) {
      sortDir = "ASC";
    }

    JPAQuery<CategoryDTO> query =
        queryFactory
            .select(Projections.constructor(CategoryDTO.class, category.id, category.name))
            .from(category)
            .where(allConditions)
            .offset(offset)
            .limit(limit);

    if (sortField != null && !sortField.isEmpty()) {
      PathBuilder<CategoryEntity> entityPath =
          new PathBuilder<>(CategoryEntity.class, "authorityEntity");

      Order order = sortDir.equalsIgnoreCase("DESC") ? Order.DESC : Order.ASC;

      if (sortField.equals("name")) {
        query.orderBy(new OrderSpecifier<>(order, category.name));
      } else {
        query.orderBy(new OrderSpecifier<>(order, entityPath.getString(sortField)));
      }
    }

    List<CategoryDTO> results = query.fetch();
    long total =
        Optional.ofNullable(
                queryFactory
                    .select(category.count())
                    .from(category)
                    .where(allConditions)
                    .fetchOne())
            .orElse(0L);

    return new PageImpl<>(results, PageRequest.of(offset / limit, limit), total);
  }

  public Optional<CategoryEntity> findById(UUID id) {
    QCategoryEntity category = QCategoryEntity.categoryEntity;
    QCategoryValueEntity categoryValue = QCategoryValueEntity.categoryValueEntity;

    List<CategoryEntity> fetch =
        queryFactory
            .selectFrom(category)
            .leftJoin(category.values, categoryValue)
            .fetchJoin()
            .where(
                category.id.eq(id),
                category.isDeleted.isFalse(),
                categoryValue.isDeleted.isFalse().or(categoryValue.id.isNull()))
            .fetch();

    if (fetch != null && !fetch.isEmpty()) {
      CategoryEntity categoryEntity = fetch.getFirst();
      return Optional.of(categoryEntity);
    }
    return Optional.empty();
  }
}
