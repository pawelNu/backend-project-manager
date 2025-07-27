package com.pawelnu.projectmanager.endpoints.project;

import com.pawelnu.projectmanager.endpoints.category.QCategoryEntity;
import com.pawelnu.projectmanager.endpoints.category.value.QCategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.company.QCompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.QEmployeeEntity;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    QCategoryValueEntity projectCategoryValue = new QCategoryValueEntity("projectCategoryValue");
    QCategoryEntity projectCategory = new QCategoryEntity("projectCategory");
    QCompanyEntity company = QCompanyEntity.companyEntity;
    QEmployeeEntity assignedEmployee = QEmployeeEntity.employeeEntity;
    QCategoryValueEntity projectPriorityValue = new QCategoryValueEntity("projectPriorityValue");
    QCategoryEntity projectPriority = new QCategoryEntity("projectPriority");

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
    QCategoryValueEntity projectCategoryValue = new QCategoryValueEntity("projectCategoryValue");
    QCategoryEntity projectCategory = new QCategoryEntity("projectCategory");
    QCompanyEntity company = QCompanyEntity.companyEntity;
    QEmployeeEntity assignedEmployee = QEmployeeEntity.employeeEntity;
    QCategoryValueEntity projectPriorityValue = new QCategoryValueEntity("projectPriorityValue");
    QCategoryEntity projectPriority = new QCategoryEntity("projectPriority");
    BooleanBuilder allConditions = new BooleanBuilder();

    String projectName = "name";
    String categoryValue = "categoryValue";
    String companyName = "companyName";
    String assignedEmployeeFiled = "assignedEmployee";
    String priorityValue = "priorityValue";

    if (params.getFilters().containsKey(projectName)) {
      allConditions.and(
          project.name.likeIgnoreCase("%" + params.getFilters().get(projectName) + "%"));
    }
    if (params.getFilters().containsKey(categoryValue)) {
      allConditions.and(
          projectCategoryValue.stringValue.likeIgnoreCase(
              "%" + params.getFilters().get(categoryValue) + "%"));
    }
    if (params.getFilters().containsKey(companyName)) {
      allConditions.and(
          company.name.likeIgnoreCase("%" + params.getFilters().get(companyName) + "%"));
    }
    if (params.getFilters().containsKey(assignedEmployeeFiled)) {
      String employeeFilter = "%" + params.getFilters().get(assignedEmployeeFiled) + "%";

      BooleanExpression employeeCondition =
          assignedEmployee
              .firstName
              .likeIgnoreCase(employeeFilter)
              .or(assignedEmployee.lastName.likeIgnoreCase(employeeFilter));

      allConditions.and(employeeCondition);
    }

    if (params.getFilters().containsKey(priorityValue)) {
      allConditions.and(
          projectPriorityValue.stringValue.likeIgnoreCase(
              "%" + params.getFilters().get(priorityValue) + "%"));
    }

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
      applySorting(query, params.getSortField(), params.getSortDir());
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
                    .leftJoin(projectCategoryValue.category, projectCategory)
                    .leftJoin(project.company, company)
                    .leftJoin(project.assignedEmployee, assignedEmployee)
                    .leftJoin(project.priorityValue, projectPriorityValue)
                    .leftJoin(projectPriorityValue.category, projectPriority)
                    .where(allConditions)
                    .fetchOne())
            .orElse(0L);
    return total;
  }

  private void applySorting(JPAQuery<ProjectEntity> query, String sortField, String sortDir) {
    QProjectEntity project = QProjectEntity.projectEntity;
    QCompanyEntity company = QCompanyEntity.companyEntity;
    QEmployeeEntity employee = QEmployeeEntity.employeeEntity;
    QCategoryEntity category = new QCategoryEntity("category");
    QCategoryEntity priority = new QCategoryEntity("priority");

    Order order = Sort.Direction.fromString(sortDir) == Sort.Direction.ASC ? Order.ASC : Order.DESC;

    switch (sortField) {
      case "name" -> query.orderBy(order == Order.ASC ? project.name.asc() : project.name.desc());
      case "categoryName" ->
          query.orderBy(order == Order.ASC ? category.name.asc() : category.name.desc());
      case "companyName" ->
          query.orderBy(order == Order.ASC ? company.name.asc() : company.name.desc());
      case "assignedEmployee" ->
          query.orderBy(order == Order.ASC ? employee.lastName.asc() : employee.lastName.desc());
      case "priorityName" ->
          query.orderBy(order == Order.ASC ? priority.name.asc() : priority.name.desc());
      default -> query.orderBy(project.name.asc());
    }
  }
}
