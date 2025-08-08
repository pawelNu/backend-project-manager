package com.pawelnu.projectmanager.endpoints.project.step.comment;

import com.pawelnu.projectmanager.endpoints.company.employee.QEmployeeEntity;
import com.pawelnu.projectmanager.endpoints.project.QProjectEntity;
import com.pawelnu.projectmanager.endpoints.project.step.QProjectStepEntity;
import com.pawelnu.projectmanager.endpoints.project.step.comment.dto.ProjectStepCommentRowDTO;
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
public class ProjectStepCommentQueryRepository {
  private final JPAQueryFactory queryFactory;
  private final QProjectStepCommentEntity projectStepComment =
      QProjectStepCommentEntity.projectStepCommentEntity;
  private final QProjectStepEntity projectStep = QProjectStepEntity.projectStepEntity;
  private final QProjectEntity project = QProjectEntity.projectEntity;
  private final QEmployeeEntity employee = QEmployeeEntity.employeeEntity;

  public Optional<ProjectStepCommentEntity> findById(UUID id) {
    //    TODO implement Optional<ProjectStepEntity> findById(UUID id)
    throw new NotImplementedException("not implemented!");
  }

  public List<ProjectStepCommentRowDTO> getList(PageableParams params) {
    BooleanBuilder allConditions = new BooleanBuilder();
    if (params.getFilters().containsKey(Field.comment)) {
      allConditions.and(
          projectStepComment.comment.likeIgnoreCase(
              "%" + params.getFilters().get(Field.comment) + "%"));
    }
    if (params.getFilters().containsKey(Field.projectStepName)) {
      allConditions.and(
          projectStep.name.likeIgnoreCase(
              "%" + params.getFilters().get(Field.projectStepName) + "%"));
    }
    if (params.getFilters().containsKey(Field.projectName)) {
      allConditions.and(
          project.name.likeIgnoreCase("%" + params.getFilters().get(Field.projectName) + "%"));
    }
    if (params.getFilters().containsKey(Field.employeeName)) {
      String employeeFilter = "%" + params.getFilters().get(Field.employeeName) + "%";

      BooleanExpression employeeCondition =
          employee
              .firstName
              .likeIgnoreCase(employeeFilter)
              .or(employee.lastName.likeIgnoreCase(employeeFilter));

      allConditions.and(employeeCondition);
    }
    if (params.getFilters().containsKey(Field.created)) {
      Instant startInstant = Shared.parseDate(params.getFilters().get(Field.created));
      allConditions.and(projectStepComment.created.loe(startInstant));
    }

    allConditions.and(projectStepComment.isDeleted.isFalse());

    JPAQuery<ProjectStepCommentRowDTO> query =
        queryFactory
            .select(
                Projections.constructor(
                    ProjectStepCommentRowDTO.class,
                    projectStepComment.id,
                    projectStepComment.comment,
                    projectStepComment.created,
                    project.id,
                    project.name,
                    projectStep.id,
                    projectStep.name,
                    employee.id,
                    employee.firstName,
                    employee.lastName,
                    Shared.totalElements(),
                    Shared.totalPages(params.getLimit())))
            .from(projectStepComment)
            .leftJoin(projectStepComment.step, projectStep)
            .leftJoin(projectStepComment.employee, employee)
            .leftJoin(projectStep.project, project)
            .where(allConditions)
            .offset(params.getOffset())
            .limit(params.getLimit());

    applySorting(query, params.getSortField(), params.getSortDir());

    return query.fetch();
  }

  private void applySorting(
      JPAQuery<ProjectStepCommentRowDTO> query, String sortField, String sortDir) {
    Order order = Sort.Direction.fromString(sortDir) == Sort.Direction.ASC ? Order.ASC : Order.DESC;
    switch (sortField) {
      case Field.created -> query.orderBy(
          order == Order.ASC
              ? projectStepComment.created.asc()
              : projectStepComment.created.desc());
      case Field.projectStepName -> query.orderBy(
          order == Order.ASC ? projectStep.name.asc() : projectStep.name.desc());
      case Field.projectName -> query.orderBy(
          order == Order.ASC ? project.name.asc() : project.name.desc());
      case Field.assignedEmployee -> query.orderBy(
          order == Order.ASC ? employee.lastName.asc() : employee.lastName.desc());
      default -> query.orderBy(projectStepComment.created.asc());
    }
  }
}
