package com.pawelnu.projectmanager.endpoints.ticket;

import com.pawelnu.projectmanager.endpoints.category.value.QCategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.QEmployeeEntity;
import com.pawelnu.projectmanager.endpoints.project.QProjectEntity;
import com.pawelnu.projectmanager.endpoints.project.step.ProjectStepEntity;
import com.pawelnu.projectmanager.endpoints.project.step.QProjectStepEntity;
import com.pawelnu.projectmanager.endpoints.ticket.dto.TicketRowDTO;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import com.pawelnu.projectmanager.utils.Shared.Field;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
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
public class TicketQueryRepository {
  private final JPAQueryFactory queryFactory;
  private final QProjectStepEntity projectStep = QProjectStepEntity.projectStepEntity;
  private final QCategoryValueEntity projectStepPriorityValue =
      new QCategoryValueEntity("projectStepPriorityValue");
  private final QProjectEntity project = QProjectEntity.projectEntity;
  private final QEmployeeEntity assignedEmployee = QEmployeeEntity.employeeEntity;
  private final QCategoryValueEntity projectPriorityValue =
      new QCategoryValueEntity("projectPriorityValue");
  private final QEmployeeEntity employee = QEmployeeEntity.employeeEntity;

  public Optional<ProjectStepEntity> findById(UUID id) {
    //    TODO implement Optional<ProjectStepEntity> findById(UUID id)
    throw new NotImplementedException("not implemented!");
  }

  // TODO fix implementation
  public List<TicketRowDTO> getList(PageableParams params) {
    BooleanBuilder allConditions = new BooleanBuilder();
    if (params.getFilters().containsKey(Field.name)) {
      allConditions.and(
          projectStep.name.likeIgnoreCase("%" + params.getFilters().get(Field.name) + "%"));
    }
    if (params.getFilters().containsKey(Field.deadline)) {
      Instant startInstant = Shared.parseDate(params.getFilters().get(Field.deadline));
      allConditions.and(projectStep.deadline.loe(startInstant));
    }
    if (params.getFilters().containsKey(Field.projectName)) {
      allConditions.and(
          project.name.likeIgnoreCase("%" + params.getFilters().get(Field.projectName) + "%"));
    }
    if (params.getFilters().containsKey(Field.priorityValue)) {
      allConditions.and(
          projectStepPriorityValue.stringValue.likeIgnoreCase(
              "%" + params.getFilters().get(Field.priorityValue) + "%"));
    }
    if (params.getFilters().containsKey(Field.assignedEmployee)) {
      String employeeFilter = "%" + params.getFilters().get(Field.assignedEmployee) + "%";

      BooleanExpression employeeCondition =
          assignedEmployee
              .firstName
              .likeIgnoreCase(employeeFilter)
              .or(assignedEmployee.lastName.likeIgnoreCase(employeeFilter));

      allConditions.and(employeeCondition);
    }

    allConditions.and(projectStep.isDeleted.isFalse());

    JPAQuery<TicketRowDTO> query =
        queryFactory
            .select(
                Projections.constructor(
                    TicketRowDTO.class,
                    projectStep.id,
                    projectStep.name,
                    project.id,
                    project.name,
                    projectPriorityValue.id,
                    projectPriorityValue.stringValue,
                    assignedEmployee.id,
                    assignedEmployee.firstName,
                    assignedEmployee.lastName,
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

    applySorting(query, params.getSortField(), params.getSortDir());

    return query.fetch();
  }

  private void applySorting(JPAQuery<TicketRowDTO> query, String sortField, String sortDir) {
    Order order = Sort.Direction.fromString(sortDir) == Sort.Direction.ASC ? Order.ASC : Order.DESC;
    switch (sortField) {
      case "name" -> query.orderBy(
          order == Order.ASC ? projectStep.name.asc() : projectStep.name.desc());
      case "projectName" -> query.orderBy(
          order == Order.ASC ? project.name.asc() : project.name.desc());
      case "assignedEmployee" -> query.orderBy(
          order == Order.ASC ? employee.lastName.asc() : employee.lastName.desc());
      case "deadline" -> query.orderBy(
          order == Order.ASC ? projectStep.deadline.asc() : projectStep.deadline.desc());
      default -> query.orderBy(projectStep.deadline.desc());
    }
  }
}
