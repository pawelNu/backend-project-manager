package com.pawelnu.projectmanager.endpoints.category;

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
    QCategoryEntity authority = QCategoryEntity.categoryEntity;
    BooleanBuilder allConditions = new BooleanBuilder();

    if (filters.containsKey(authority.name.getMetadata().getName())) {
      allConditions.and(
          authority.name.likeIgnoreCase(
              "%" + filters.get(authority.name.getMetadata().getName()) + "%"));
    }
    allConditions.and(authority.isDeleted.isFalse());
    if (sortDir == null || sortDir.isEmpty()) {
      sortDir = "ASC";
    }

    JPAQuery<CategoryDTO> query =
        queryFactory
            .select(Projections.constructor(CategoryDTO.class, authority.id, authority.name))
            .from(authority)
            .where(allConditions)
            .offset(offset)
            .limit(limit);

    if (sortField != null && !sortField.isEmpty()) {
      PathBuilder<CategoryEntity> entityPath =
          new PathBuilder<>(CategoryEntity.class, "authorityEntity");

      Order order = sortDir.equalsIgnoreCase("DESC") ? Order.DESC : Order.ASC;

      if (sortField.equals("name")) {
        query.orderBy(new OrderSpecifier<>(order, authority.name));
      } else {
        query.orderBy(new OrderSpecifier<>(order, entityPath.getString(sortField)));
      }
    }

    List<CategoryDTO> results = query.fetch();
    long total =
        Optional.ofNullable(
                queryFactory
                    .select(authority.count())
                    .from(authority)
                    .where(allConditions)
                    .fetchOne())
            .orElse(0L);

    return new PageImpl<>(results, PageRequest.of(offset / limit, limit), total);
  }

  public Optional<CategoryEntity> findById(UUID id) {
    QCategoryEntity authority = QCategoryEntity.categoryEntity;

    List<CategoryEntity> fetch =
        queryFactory
            .selectFrom(authority)
            .where(authority.id.eq(id).and(authority.isDeleted.isFalse()))
            .fetch();

    if (fetch != null && !fetch.isEmpty()) {
      CategoryEntity authorityEntity = fetch.getFirst();
      return Optional.of(authorityEntity);
    }
    return Optional.empty();
  }
}
