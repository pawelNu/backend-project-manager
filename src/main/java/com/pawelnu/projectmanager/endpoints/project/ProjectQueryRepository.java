package com.pawelnu.projectmanager.endpoints.project;

import com.pawelnu.projectmanager.endpoints.category.QCategoryEntity;
import com.pawelnu.projectmanager.endpoints.category.value.QCategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.company.QCompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.QEmployeeEntity;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
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
public class ProjectQueryRepository {
  private final JPAQueryFactory queryFactory;

  public Optional<ProjectEntity> findById(UUID id) {
    QProjectEntity project = QProjectEntity.projectEntity;
    QCategoryValueEntity projectCategoryValue = QCategoryValueEntity.categoryValueEntity;
    QCategoryEntity projectCategory = QCategoryEntity.categoryEntity;
    QCompanyEntity company = QCompanyEntity.companyEntity;
    QEmployeeEntity assignedEmployee = QEmployeeEntity.employeeEntity;
    QCategoryValueEntity projectPriorityValue = QCategoryValueEntity.categoryValueEntity;
    QCategoryEntity projectPriority = QCategoryEntity.categoryEntity;

    List<ProjectEntity> fetch =
        queryFactory
            .selectFrom(project)
            .leftJoin(project.categoryValue, projectCategoryValue)
            .fetchJoin()
            .leftJoin(projectCategoryValue.category, projectCategory)
            .fetchJoin()
            .leftJoin(project.company, company)
            .fetchJoin()
            .leftJoin(project.assignedEmployee, assignedEmployee)
            .fetchJoin()
            .leftJoin(project.priorityValue, projectPriorityValue)
            .fetchJoin()
            .leftJoin(projectPriorityValue.category, projectPriority)
            .fetchJoin()
            .where(project.id.eq(id).and(project.isDeleted.isFalse()))
            .fetch();

    if (fetch != null && !fetch.isEmpty()) {
      ProjectEntity projectEntity = fetch.getFirst();
      return Optional.of(projectEntity);
    }
    return Optional.empty();
  }

  public Page<ProjectEntity> getList(PageableParams params) {
    QProjectEntity project = QProjectEntity.projectEntity;
    QCategoryValueEntity projectCategoryValue = QCategoryValueEntity.categoryValueEntity;
    QCategoryEntity projectCategory = QCategoryEntity.categoryEntity;
    QCompanyEntity company = QCompanyEntity.companyEntity;
    QEmployeeEntity assignedEmployee = QEmployeeEntity.employeeEntity;
    QCategoryValueEntity projectPriorityValue = QCategoryValueEntity.categoryValueEntity;
    QCategoryEntity projectPriority = QCategoryEntity.categoryEntity;
    BooleanBuilder allConditions = new BooleanBuilder();

    //    if (params.getFilters().containsKey("companyName")) {
    //      allConditions.and(
    //          company.name.likeIgnoreCase("%" + params.getFilters().get("companyName") + "%"));
    //    }
    //    if (params.getFilters().containsKey(employee.firstName.getMetadata().getName())) {
    //      allConditions.and(
    //          employee.firstName.likeIgnoreCase(
    //              "%" + params.getFilters().get(employee.firstName.getMetadata().getName()) +
    // "%"));
    //    }
    //    if (params.getFilters().containsKey(employee.lastName.getMetadata().getName())) {
    //      allConditions.and(
    //          employee.lastName.likeIgnoreCase(
    //              "%" + params.getFilters().get(employee.lastName.getMetadata().getName()) +
    // "%"));
    //    }
    //    if (params.getFilters().containsKey(employee.username.getMetadata().getName())) {
    //      allConditions.and(
    //          employee.username.likeIgnoreCase(
    //              "%" + params.getFilters().get(employee.username.getMetadata().getName()) +
    // "%"));
    //    }
    //    if (params.getFilters().containsKey(employee.email.getMetadata().getName())) {
    //      allConditions.and(
    //          employee.email.likeIgnoreCase(
    //              "%" + params.getFilters().get(employee.email.getMetadata().getName()) + "%"));
    //    }
    //    if (params.getFilters().containsKey(employee.phoneNumber.getMetadata().getName())) {
    //      allConditions.and(
    //          employee.phoneNumber.likeIgnoreCase(
    //              "%" + params.getFilters().get(employee.phoneNumber.getMetadata().getName()) +
    // "%"));
    //    }
    allConditions.and(project.isDeleted.isFalse());

    JPAQuery<ProjectEntity> query =
        queryFactory
            .selectFrom(project)
            .leftJoin(project.categoryValue, projectCategoryValue)
            .fetchJoin()
            .leftJoin(projectCategoryValue.category, projectCategory)
            .fetchJoin()
            .leftJoin(project.company, company)
            .fetchJoin()
            .leftJoin(project.assignedEmployee, assignedEmployee)
            .fetchJoin()
            .leftJoin(project.priorityValue, projectPriorityValue)
            .fetchJoin()
            .leftJoin(projectPriorityValue.category, projectPriority)
            .fetchJoin()
            .where(allConditions)
            .offset(params.getOffset())
            .limit(params.getLimit());

    if (!params.getSortField().isEmpty()) {
      if (params.getSortField().equals("companyName")) {
        query.orderBy(
            params.getSortDir().equalsIgnoreCase("DESC")
                ? company.name.desc()
                : company.name.asc());
      } else {
        PathBuilder<EmployeeEntity> entityPath =
            new PathBuilder<>(EmployeeEntity.class, "employeeEntity");
        query.orderBy(
            new OrderSpecifier<>(
                Sort.Direction.fromString(params.getSortDir()) == Sort.Direction.ASC
                    ? Order.ASC
                    : Order.DESC,
                entityPath.getString(params.getSortField())));
      }
    }

    List<ProjectEntity> results = query.fetch();
    long total =
        countProjects(
            project,
            projectCategoryValue,
            projectCategory,
            company,
            assignedEmployee,
            projectPriorityValue,
            projectPriority,
            allConditions);

    PageRequest pageable =
        PageRequest.of(params.getOffset() / params.getLimit(), params.getLimit());
    return new PageImpl<>(results, pageable, total);
  }

  private long countProjects(
      QProjectEntity project,
      QCategoryValueEntity projectCategoryValue,
      QCategoryEntity projectCategory,
      QCompanyEntity company,
      QEmployeeEntity assignedEmployee,
      QCategoryValueEntity projectPriorityValue,
      QCategoryEntity projectPriority,
      BooleanBuilder allConditions) {
    long total =
        Optional.ofNullable(
                queryFactory
                    .select(project.count())
                    .from(project)
                    .leftJoin(project.categoryValue, projectCategoryValue)
                    .fetchJoin()
                    .leftJoin(projectCategoryValue.category, projectCategory)
                    .fetchJoin()
                    .leftJoin(project.company, company)
                    .fetchJoin()
                    .leftJoin(project.assignedEmployee, assignedEmployee)
                    .fetchJoin()
                    .leftJoin(project.priorityValue, projectPriorityValue)
                    .fetchJoin()
                    .leftJoin(projectPriorityValue.category, projectPriority)
                    .fetchJoin()
                    .where(allConditions)
                    .fetchOne())
            .orElse(0L);
    return total;
  }
}
