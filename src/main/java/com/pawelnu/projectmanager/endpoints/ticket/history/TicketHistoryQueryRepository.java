package com.pawelnu.projectmanager.endpoints.ticket.history;

import com.pawelnu.projectmanager.endpoints.category.value.QCategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.QEmployeeEntity;
import com.pawelnu.projectmanager.endpoints.ticket.QTicketEntity;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketHistoryRowDTO;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TicketHistoryQueryRepository {
  private final JPAQueryFactory queryFactory;
  private final EntityManager em;
  private final QTicketHistoryEntity history = QTicketHistoryEntity.ticketHistoryEntity;
  private final QTicketEntity ticket = QTicketEntity.ticketEntity;
  private final QCategoryValueEntity fromStatus = new QCategoryValueEntity("fromStatus");
  private final QCategoryValueEntity toStatus = new QCategoryValueEntity("toStatus");
  private final QEmployeeEntity fromEmployee = new QEmployeeEntity("fromEmployee");
  private final QEmployeeEntity toEmployee = new QEmployeeEntity("toEmployee");

  public List<TicketHistoryRowDTO> getList(PageableParams params) {
    BooleanBuilder allConditions = prepareConditions(params);
    JPAQuery<TicketHistoryRowDTO> query =
        queryFactory
            .select(
                Projections.constructor(
                    TicketHistoryRowDTO.class,
                    history.id,
                    ticket.id,
                    ticket.number,
                    ticket.title,
                    fromStatus.id,
                    fromStatus.stringValue,
                    toStatus.id,
                    toStatus.stringValue,
                    fromEmployee.id,
                    fromEmployee.firstName,
                    fromEmployee.lastName,
                    toEmployee.id,
                    toEmployee.firstName,
                    toEmployee.lastName,
                    history.comment,
                    history.created,
                    Shared.totalElements(),
                    Shared.totalPages(params.getLimit())))
            .from(history)
            .leftJoin(history.ticket, ticket)
            .leftJoin(history.fromStatus, fromStatus)
            .leftJoin(history.toStatus, toStatus)
            .leftJoin(history.fromEmployee, fromEmployee)
            .leftJoin(history.toEmployee, toEmployee)
            .where(allConditions)
            .offset(params.getOffset())
            .limit(params.getLimit());

    applySorting(query, params.getSortField(), params.getSortDir());

    return query.fetch();
  }

  private BooleanBuilder prepareConditions(PageableParams params) {
    BooleanBuilder allConditions = new BooleanBuilder();
    if (params.getFilters().containsKey(Field.comment)) {
      allConditions.and(
          history.comment.likeIgnoreCase("%" + params.getFilters().get(Field.comment) + "%"));
    }
    if (params.getFilters().containsKey(Field.created)) {
      Instant startInstant = Shared.parseDate(params.getFilters().get(Field.created));
      allConditions.and(history.created.loe(startInstant));
    }
    if (params.getFilters().containsKey(Field.ticketNumber)) {
      allConditions.and(ticket.number.eq(params.getFilters().get(Field.ticketNumber)));
    }
    if (params.getFilters().containsKey(Field.ticketTitle)) {
      allConditions.and(
          ticket.title.likeIgnoreCase("%" + params.getFilters().get(Field.ticketTitle) + "%"));
    }

    allConditions.and(history.isDeleted.isFalse());
    return allConditions;
  }

  private void applySorting(JPAQuery<TicketHistoryRowDTO> query, String sortField, String sortDir) {
    Order order = Sort.Direction.fromString(sortDir) == Sort.Direction.ASC ? Order.ASC : Order.DESC;
    switch (sortField) {
//      case Field.ticketNumber -> query.orderBy(
//          order == Order.ASC ? ticket.number.asc() : ticket.number.desc());
//      case Field.ticketTitle -> query.orderBy(
//          order == Order.ASC ? ticket.title.asc() : ticket.title.desc());
      case Field.created -> query.orderBy(
          order == Order.ASC ? history.created.asc() : history.created.desc());
      default -> query.orderBy(history.created.asc());
    }
  }
}
