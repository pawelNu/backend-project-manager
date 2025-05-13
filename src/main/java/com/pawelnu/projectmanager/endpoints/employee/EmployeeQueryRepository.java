package com.pawelnu.projectmanager.endpoints.employee;

import com.pawelnu.projectmanager.endpoints.authority.QAuthorityEntity;
import com.pawelnu.projectmanager.endpoints.authority.QEmployeeAuthorityEntity;
import com.pawelnu.projectmanager.endpoints.company.QCompanyEntity;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EmployeeQueryRepository {

  private final JPAQueryFactory queryFactory;
  private final EmployeeMapper employeeMapper;

  public Page<EmployeeEntity> getEmployeeList(
      Map<String, String> filters, int offset, int limit, String sortDir, String sortField) {
    log.info(filters.toString());
    QCompanyEntity company = QCompanyEntity.companyEntity;
    QEmployeeEntity employee = QEmployeeEntity.employeeEntity;
    BooleanBuilder allConditions = new BooleanBuilder();

    if (filters.containsKey("companyName")) {
      allConditions.and(company.name.likeIgnoreCase("%" + filters.get("companyName") + "%"));
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
    if (filters.containsKey(employee.username.getMetadata().getName())) {
      allConditions.and(
          employee.username.likeIgnoreCase(
              "%" + filters.get(employee.username.getMetadata().getName()) + "%"));
    }
    if (filters.containsKey(employee.email.getMetadata().getName())) {
      allConditions.and(
          employee.email.likeIgnoreCase(
              "%" + filters.get(employee.email.getMetadata().getName()) + "%"));
    }
    if (filters.containsKey(employee.phoneNumber.getMetadata().getName())) {
      allConditions.and(
          employee.phoneNumber.likeIgnoreCase(
              "%" + filters.get(employee.phoneNumber.getMetadata().getName()) + "%"));
    }
    allConditions.and(employee.isDeleted.isFalse());

    JPAQuery<EmployeeEntity> query =
        queryFactory
            .selectFrom(employee)
            .leftJoin(employee.company, company)
            .fetchJoin()
            .where(allConditions)
            .offset(offset)
            .limit(limit);

    if (!sortField.isEmpty()) {
      if (sortField.equals("companyName")) {
        query.orderBy(sortDir.equalsIgnoreCase("DESC") ? company.name.desc() : company.name.asc());
      } else {
        PathBuilder<EmployeeEntity> entityPath =
            new PathBuilder<>(EmployeeEntity.class, "employeeEntity");
        query.orderBy(
            new OrderSpecifier<>(
                Sort.Direction.fromString(sortDir) == Sort.Direction.ASC ? Order.ASC : Order.DESC,
                entityPath.getString(sortField)));
      }
    }

    List<EmployeeEntity> results = query.fetch();
    long total =
        Optional.ofNullable(
                queryFactory
                    .select(employee.count())
                    .from(employee)
                    .leftJoin(employee.company, company)
                    .where(allConditions)
                    .fetchOne())
            .orElse(0L);

    return new PageImpl<>(results, PageRequest.of(offset / limit, limit), total);
  }

  public List<EmployeeAuthorityRowDTO> findByUsernameWithAuthorities(String username) {
    QEmployeeEntity employee = QEmployeeEntity.employeeEntity;
    QEmployeeAuthorityEntity employeeAuthority = QEmployeeAuthorityEntity.employeeAuthorityEntity;
    QAuthorityEntity authority = QAuthorityEntity.authorityEntity;

    List<EmployeeAuthorityRowDTO> result =
        queryFactory
            .select(
                Projections.constructor(
                    EmployeeAuthorityRowDTO.class,
                    employee.id,
                    employee.username,
                    employee.email,
                    employee.password,
                    authority.name))
            .from(employee)
            .leftJoin(employee.authorities, employeeAuthority)
            .leftJoin(employeeAuthority.authority, authority)
            .where(employee.username.eq(username))
            .fetch();
    if (result != null && !result.isEmpty()) {
      return result;
    }
    return List.of();
  }
}
