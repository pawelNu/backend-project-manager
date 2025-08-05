package com.pawelnu.projectmanager.endpoints.authority;

import com.pawelnu.projectmanager.endpoints.authority.dto.AuthorityDTO;
import com.pawelnu.projectmanager.endpoints.company.employee.QEmployeeEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.authority.QEmployeeAuthorityEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
                    authority.nameFrontend,
                    ConstantImpl.create(Collections.emptyList())))
            .from(authority)
            .where(allConditions)
            .offset(offset)
            .limit(limit);

    Order order = Sort.Direction.fromString(sortDir) == Sort.Direction.ASC ? Order.ASC : Order.DESC;
    if (sortField.equals("nameBackend")) {
      query.orderBy(
          order == Order.ASC ? authority.nameBackend.asc() : authority.nameBackend.desc());
      //      TODO add others fields
    } else {
      query.orderBy(authority.nameBackend.asc());
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
    QEmployeeAuthorityEntity employeeAuthority = QEmployeeAuthorityEntity.employeeAuthorityEntity;
    QEmployeeEntity employee = QEmployeeEntity.employeeEntity;

    List<AuthorityEntity> fetch =
        queryFactory
            .selectFrom(authority)
            .leftJoin(authority.employeeAuthorities, employeeAuthority)
            .fetchJoin()
            .leftJoin(employeeAuthority.employee, employee)
            .fetchJoin()
            .where(authority.id.eq(id).and(authority.isDeleted.isFalse()))
            .fetch();

    if (fetch != null && !fetch.isEmpty()) {
      AuthorityEntity authorityEntity = fetch.getFirst();
      return Optional.of(authorityEntity);
    }
    return Optional.empty();
  }
}
