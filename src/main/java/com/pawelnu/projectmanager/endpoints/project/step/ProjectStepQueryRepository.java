package com.pawelnu.projectmanager.endpoints.project.step;

import com.pawelnu.projectmanager.endpoints.category.QCategoryEntity;
import com.pawelnu.projectmanager.endpoints.category.value.QCategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.company.QCompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.QEmployeeEntity;
import com.pawelnu.projectmanager.endpoints.project.QProjectEntity;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepRowDTO;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProjectStepQueryRepository {
  private final JPAQueryFactory queryFactory;
  private final QProjectStepEntity projectStep = QProjectStepEntity.projectStepEntity;
  private final QCategoryValueEntity projectStepPriorityValue =
      new QCategoryValueEntity("projectStepPriorityValue");
  private final QProjectEntity project = QProjectEntity.projectEntity;
  private final QCompanyEntity company = QCompanyEntity.companyEntity;
  private final QEmployeeEntity assignedEmployee = QEmployeeEntity.employeeEntity;
  private final QCategoryValueEntity projectPriorityValue =
      new QCategoryValueEntity("projectPriorityValue");
  private final BooleanBuilder allConditions = new BooleanBuilder();
  private final QEmployeeEntity employee = QEmployeeEntity.employeeEntity;
  private final QCategoryEntity category = new QCategoryEntity("category");
  private final QCategoryEntity priority = new QCategoryEntity("priority");

  public Optional<ProjectStepEntity> findById(UUID id) {
    //    TODO implement Optional<ProjectStepEntity> findById(UUID id)
    throw new NotImplementedException("not implemented!");
  }

  public List<ProjectStepRowDTO> getList(PageableParams params) {
    String projectName = "name";
    String companyName = "companyName";
    String assignedEmployeeFiled = "assignedEmployee";
    String priorityValue = "priorityValue";

    if (params.getFilters().containsKey(projectName)) {
      allConditions.and(
          project.name.likeIgnoreCase("%" + params.getFilters().get(projectName) + "%"));
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

    allConditions.and(projectStep.isDeleted.isFalse());

    JPAQuery<ProjectStepRowDTO> query =
        queryFactory
            .select(
                Projections.constructor(
                    ProjectStepRowDTO.class,
                    projectStep.id,
                    projectStep.name,
                    project.id,
                    project.name,
                    projectPriorityValue.id,
                    projectPriorityValue.stringValue,
                    assignedEmployee.id,
                    assignedEmployee.firstName,
                    projectStep.deadline,
                    Shared.totalElements(),
                    Shared.totalPages(params.getLimit())))
            .from(projectStep)
            .leftJoin(projectStep.project, project)
            .leftJoin(project.priorityValue, projectPriorityValue)
            .leftJoin(projectStep.assignedEmployee, assignedEmployee)
            .leftJoin(projectStep.priority, projectStepPriorityValue)
            .where(allConditions)
            .offset(params.getOffset())
            .limit(params.getLimit());

    if (!params.getSortField().isEmpty()) {
      applySorting(query, params.getSortField(), params.getSortDir());
    }

    return query.fetch();
  }

  private void applySorting(JPAQuery<ProjectStepRowDTO> query, String sortField, String sortDir) {
    Order order = Sort.Direction.fromString(sortDir) == Sort.Direction.ASC ? Order.ASC : Order.DESC;

    switch (sortField) {
      case "categoryName":
        query.orderBy(order == Order.ASC ? category.name.asc() : category.name.desc());
        break;
      case "companyName":
        query.orderBy(order == Order.ASC ? company.name.asc() : company.name.desc());
        break;
      case "assignedEmployee":
        query.orderBy(order == Order.ASC ? employee.lastName.asc() : employee.lastName.desc());
        break;
      case "priorityName":
        query.orderBy(order == Order.ASC ? priority.name.asc() : priority.name.desc());
        break;
      default:
        query.orderBy(order == Order.ASC ? project.name.asc() : project.name.desc());
    }
  }
}
