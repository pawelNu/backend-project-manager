package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import com.pawelnu.projectmanager.endpoints.authority.QAuthorityEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.QEmployeeEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EmployeeAuthorityQueryRepository {

  private final JPAQueryFactory queryFactory;

  public Page<EmployeeAuthorityEntity> filter(
      Map<String, String> filters, int offset, int limit, String sortDir, String sortField) {
    log.info(filters.toString());
    QEmployeeAuthorityEntity employeeAuthority = QEmployeeAuthorityEntity.employeeAuthorityEntity;
    QAuthorityEntity authority = QAuthorityEntity.authorityEntity;
    QEmployeeEntity employee = QEmployeeEntity.employeeEntity;
    BooleanBuilder allConditions = new BooleanBuilder();

    if (filters.containsKey(authority.name.getMetadata().getName())) {
      allConditions.and(
          authority.name.likeIgnoreCase(
              "%" + filters.get(authority.name.getMetadata().getName()) + "%"));
    }
    if (filters.containsKey(employee.firstName.getMetadata().getName())) {
      allConditions.and(
          employee.firstName.likeIgnoreCase(
              "%" + filters.get(employee.firstName.getMetadata().getName()) + "%"));
    }
    if (filters.containsKey(employee.lastName.getMetadata().getName())) {
      allConditions.and(
          employee.lastName.likeIgnoreCase(
              "%" + filters.get(employee.lastName.getMetadata().getName()) + "%"));
    }

    allConditions.and(employeeAuthority.isDeleted.isFalse());

    JPAQuery<EmployeeAuthorityEntity> query =
        queryFactory
            .selectFrom(employeeAuthority)
            .leftJoin(employeeAuthority.authority, authority)
            .fetchJoin()
            .leftJoin(employeeAuthority.employee, employee)
            .fetchJoin()
            .where(allConditions)
            .offset(offset)
            .limit(limit);

    if (!sortField.isEmpty()) {
      if (sortField.equals(authority.name.getMetadata().getName())) {
        query.orderBy(
            sortDir.equalsIgnoreCase("DESC") ? authority.name.desc() : authority.name.asc());
      } else {
        PathBuilder<EmployeeAuthorityEntity> entityPath =
            new PathBuilder<>(EmployeeAuthorityEntity.class, "employeeAuthorityEntity");
        query.orderBy(
            new OrderSpecifier<>(
                Sort.Direction.fromString(sortDir) == Sort.Direction.ASC ? Order.ASC : Order.DESC,
                entityPath.getString(sortField)));
      }
    }

    List<EmployeeAuthorityEntity> results = query.fetch();
    long total =
        Optional.ofNullable(
                queryFactory
                    .select(employeeAuthority.count())
                    .from(employeeAuthority)
                    .leftJoin(employeeAuthority.authority, authority)
                    .leftJoin(employeeAuthority.employee, employee)
                    .where(allConditions)
                    .fetchOne())
            .orElse(0L);

    return new PageImpl<>(results, PageRequest.of(offset / limit, limit), total);
  }
}
