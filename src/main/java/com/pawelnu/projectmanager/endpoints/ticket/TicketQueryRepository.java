package com.pawelnu.projectmanager.endpoints.ticket;

import com.pawelnu.projectmanager.endpoints.category.QCategoryEntity;
import com.pawelnu.projectmanager.endpoints.category.value.QCategoryValueEntity;
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
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
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
  private final EntityManager em;
  private final QTicketEntity ticket = QTicketEntity.ticketEntity;
  private final QCategoryEntity category = new QCategoryEntity("category");
  private final QCategoryEntity priority = new QCategoryEntity("priority");
  private final QCategoryValueEntity categoryValue = new QCategoryValueEntity("categoryValue");
  private final QCategoryValueEntity priorityValue = new QCategoryValueEntity("priorityValue");
  private final QProjectEntity project = QProjectEntity.projectEntity;
  private final QProjectStepEntity projectStep = QProjectStepEntity.projectStepEntity;

  public void createSequence(String sequenceName) {
    if (!sequenceName.matches("^[a-zA-Z0-9_]+$")) throw new IllegalArgumentException();
    em.createNativeQuery("CREATE SEQUENCE " + sequenceName + " START 1 MAXVALUE 99999 INCREMENT 1")
        .executeUpdate();
  }

  public Optional<ProjectStepEntity> findById(UUID id) {
    //    TODO implement Optional<ProjectStepEntity> findById(UUID id)
    throw new NotImplementedException("not implemented!");
  }

  public List<TicketRowDTO> getList(PageableParams params) {
    BooleanBuilder allConditions = prepareConditions(params);
    JPAQuery<TicketRowDTO> query =
        queryFactory
            .select(
                Projections.constructor(
                    TicketRowDTO.class,
                    ticket.id,
                    ticket.number,
                    ticket.title,
                    ticket.deadline,
                    ticket.additionalDetails,
                    category.id,
                    category.name,
                    categoryValue.id,
                    categoryValue.stringValue,
                    priority.id,
                    priority.name,
                    priorityValue.id,
                    priorityValue.stringValue,
                    project.id,
                    project.name,
                    projectStep.id,
                    projectStep.name,
                    Shared.totalElements(),
                    Shared.totalPages(params.getLimit())))
            .from(ticket)
            .leftJoin(ticket.category, categoryValue)
            .leftJoin(categoryValue.category, category)
            .leftJoin(ticket.priority, priorityValue)
            .leftJoin(priorityValue.category, priority)
            .leftJoin(ticket.project, project)
            .leftJoin(ticket.step, projectStep)
            .where(allConditions)
            .offset(params.getOffset())
            .limit(params.getLimit());

    applySorting(query, params.getSortField(), params.getSortDir());

    return query.fetch();
  }

  private BooleanBuilder prepareConditions(PageableParams params) {
    BooleanBuilder allConditions = new BooleanBuilder();
    if (params.getFilters().containsKey(Field.number)) {
      allConditions.and(ticket.number.eq(params.getFilters().get(Field.number)));
    }
    if (params.getFilters().containsKey(Field.title)) {
      allConditions.and(
          ticket.title.likeIgnoreCase("%" + params.getFilters().get(Field.title) + "%"));
    }
    if (params.getFilters().containsKey(Field.deadline)) {
      Instant startInstant = Shared.parseDate(params.getFilters().get(Field.deadline));
      allConditions.and(ticket.deadline.loe(startInstant));
    }
    if (params.getFilters().containsKey(Field.additionalDetails)) {
      allConditions.and(
          ticket.additionalDetails.likeIgnoreCase(
              "%" + params.getFilters().get(Field.additionalDetails) + "%"));
    }
    if (params.getFilters().containsKey(Field.categoryValue)) {
      allConditions.and(
          ticket.category.stringValue.likeIgnoreCase(
              "%" + params.getFilters().get(Field.categoryValue) + "%"));
    }
    if (params.getFilters().containsKey(Field.priorityValue)) {
      allConditions.and(
          ticket.priority.stringValue.likeIgnoreCase(
              "%" + params.getFilters().get(Field.priorityValue) + "%"));
    }
    if (params.getFilters().containsKey(Field.projectName)) {
      allConditions.and(
          ticket.project.name.likeIgnoreCase(
              "%" + params.getFilters().get(Field.projectName) + "%"));
    }
    if (params.getFilters().containsKey(Field.projectStepName)) {
      allConditions.and(
          ticket.step.name.likeIgnoreCase(
              "%" + params.getFilters().get(Field.projectStepName) + "%"));
    }
    allConditions.and(projectStep.isDeleted.isFalse());
    return allConditions;
  }

  private void applySorting(JPAQuery<TicketRowDTO> query, String sortField, String sortDir) {
    Order order = Sort.Direction.fromString(sortDir) == Sort.Direction.ASC ? Order.ASC : Order.DESC;
    switch (sortField) {
      case Field.number -> query.orderBy(
          order == Order.ASC ? ticket.number.asc() : ticket.number.desc());
      case Field.title -> query.orderBy(
          order == Order.ASC ? ticket.title.asc() : ticket.title.desc());
      case Field.deadline -> query.orderBy(
          order == Order.ASC ? ticket.deadline.asc() : ticket.deadline.desc());
      case Field.categoryValue -> query.orderBy(
          order == Order.ASC ? categoryValue.stringValue.asc() : categoryValue.stringValue.desc());
      case Field.priorityValue -> query.orderBy(
          order == Order.ASC ? priorityValue.stringValue.asc() : priorityValue.stringValue.desc());
      case Field.projectName -> query.orderBy(
          order == Order.ASC ? project.name.asc() : project.name.desc());
      case Field.projectStepName -> query.orderBy(
          order == Order.ASC ? projectStep.name.asc() : projectStep.name.desc());
      default -> query.orderBy(ticket.deadline.desc());
    }
  }
}
