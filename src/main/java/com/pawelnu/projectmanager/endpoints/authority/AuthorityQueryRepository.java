package com.pawelnu.projectmanager.endpoints.authority;

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
public class AuthorityQueryRepository {

  private final JPAQueryFactory queryFactory;

  public Page<AuthorityDTO> filter(
      Map<String, String> filters, int offset, int limit, String sortDir, String sortField) {
    QAuthorityEntity authority = QAuthorityEntity.authorityEntity;
    BooleanBuilder allConditions = new BooleanBuilder();

    if (filters.containsKey(authority.nameBackend.getMetadata().getName())) {
      allConditions.and(
          authority.nameBackend.likeIgnoreCase(
              "%" + filters.get(authority.nameBackend.getMetadata().getName()) + "%"));
    }
    allConditions.and(authority.isDeleted.isFalse());
    if (sortDir == null || sortDir.isEmpty()) {
      sortDir = "ASC";
    }

    JPAQuery<AuthorityDTO> query =
        queryFactory
            .select(
                Projections.constructor(
                    AuthorityDTO.class,
                    authority.id,
                    authority.nameBackend,
                    authority.nameFrontend))
            .from(authority)
            .where(allConditions)
            .offset(offset)
            .limit(limit);

    if (sortField != null && !sortField.isEmpty()) {
      PathBuilder<AuthorityEntity> entityPath =
          new PathBuilder<>(AuthorityEntity.class, "authorityEntity");

      Order order = sortDir.equalsIgnoreCase("DESC") ? Order.DESC : Order.ASC;

      if (sortField.equals("name")) {
        query.orderBy(new OrderSpecifier<>(order, authority.nameBackend));
      } else {
        query.orderBy(new OrderSpecifier<>(order, entityPath.getString(sortField)));
      }
    }

    List<AuthorityDTO> results = query.fetch();
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

  public Optional<AuthorityEntity> findById(UUID id) {
    QAuthorityEntity authority = QAuthorityEntity.authorityEntity;

    List<AuthorityEntity> fetch =
        queryFactory
            .selectFrom(authority)
            .where(authority.id.eq(id).and(authority.isDeleted.isFalse()))
            .fetch();

    if (fetch != null && !fetch.isEmpty()) {
      AuthorityEntity authorityEntity = fetch.getFirst();
      return Optional.of(authorityEntity);
    }
    return Optional.empty();
  }
}
