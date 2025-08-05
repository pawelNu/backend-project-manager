package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.endpoints.category.value.QCategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.company.address.QCompanyAddressEntity;
import com.pawelnu.projectmanager.utils.Shared;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CompanyQueryRepository {

  private final CompanyRepository companyRepository;
  private final JPAQueryFactory queryFactory;

  public Page<CompanyEntity> filter(CompanyFilterRequestDTO body) {
    QCompanyEntity company = QCompanyEntity.companyEntity;
    BooleanBuilder allConditions = new BooleanBuilder();
    if (body.getFilters().getNames() != null && !body.getFilters().getNames().isEmpty()) {
      BooleanBuilder namesCondition = new BooleanBuilder();
      for (String name : body.getFilters().getNames()) {
        namesCondition.or(company.name.likeIgnoreCase("%" + name.toLowerCase() + "%"));
      }
      allConditions.or(namesCondition);
    }
    if (body.getFilters().getNips() != null && !body.getFilters().getNips().isEmpty()) {
      allConditions.or(company.nip.in(body.getFilters().getNips()));
    }
    if (body.getFilters().getRegons() != null && !body.getFilters().getRegons().isEmpty()) {
      allConditions.or(company.regon.in(body.getFilters().getRegons()));
    }
    Pageable pageable =
        Shared.preparePageable(
            body.getPage().getPageNumber(),
            body.getPage().getPageSize(),
            body.getPage().getSortedBy(),
            body.getPage().getDirection());
    return companyRepository.findAll(allConditions, pageable);
  }

  public Page<CompanySimpleDTO> filter(
      Map<String, String> filters, int offset, int limit, String sortDir, String sortField) {
    QCompanyEntity company = QCompanyEntity.companyEntity;
    QCategoryValueEntity status = QCategoryValueEntity.categoryValueEntity;
    BooleanBuilder allConditions = new BooleanBuilder();

    if (filters.containsKey(company.name.getMetadata().getName())) {
      allConditions.and(
          company.name.likeIgnoreCase(
              "%" + filters.get(company.name.getMetadata().getName()) + "%"));
    }
    if (filters.containsKey(company.nip.getMetadata().getName())) {
      allConditions.and(company.nip.eq(filters.get(company.nip.getMetadata().getName())));
    }
    if (filters.containsKey(company.regon.getMetadata().getName())) {
      allConditions.and(company.regon.eq(filters.get(company.regon.getMetadata().getName())));
    }
    if (filters.containsKey(company.status.getMetadata().getName())) {
      allConditions.and(
          status.stringValue.likeIgnoreCase((filters.get(company.status.getMetadata().getName()))));
    }
    allConditions.and(company.isDeleted.isFalse());
    if (sortDir == null || sortDir.isEmpty()) {
      sortDir = "ASC";
    }

    JPAQuery<CompanySimpleDTO> query =
        queryFactory
            .select(
                Projections.constructor(
                    CompanySimpleDTO.class,
                    company.id,
                    company.name,
                    company.nip,
                    company.regon,
                    status.stringValue,
                    company.website))
            .from(company)
            .leftJoin(company.status, status)
            .where(allConditions)
            .offset(offset)
            .limit(limit);

    Order order = Sort.Direction.fromString(sortDir) == Sort.Direction.ASC ? Order.ASC : Order.DESC;
    switch (sortField) {
      case "name" -> query.orderBy(order == Order.ASC ? company.name.asc() : company.name.desc());
      case "nip" -> query.orderBy(order == Order.ASC ? company.nip.asc() : company.nip.desc());
      case "regon" -> query.orderBy(
          order == Order.ASC ? company.regon.asc() : company.regon.desc());
      case "status" -> query.orderBy(
          order == Order.ASC ? status.stringValue.asc() : status.stringValue.desc());
      case "website" -> query.orderBy(
          order == Order.ASC ? company.website.asc() : company.website.desc());
      default -> query.orderBy(company.name.asc());
    }

    List<CompanySimpleDTO> results = query.fetch();
    long total =
        Optional.ofNullable(
                queryFactory
                    .select(company.count())
                    .from(company)
                    .leftJoin(company.status, status)
                    .where(allConditions)
                    .fetchOne())
            .orElse(0L);

    return new PageImpl<>(results, PageRequest.of(offset / limit, limit), total);
  }

  public Optional<CompanyEntity> findById(UUID id) {
    QCompanyEntity company = QCompanyEntity.companyEntity;
    QCompanyAddressEntity address = QCompanyAddressEntity.companyAddressEntity;

    List<CompanyEntity> fetch =
        queryFactory
            .selectFrom(company)
            .leftJoin(company.addresses, address)
            .fetchJoin()
            .where(company.id.eq(id).and(company.isDeleted.isFalse()))
            .fetch();

    if (fetch != null && !fetch.isEmpty()) {
      CompanyEntity companyEntity = fetch.getFirst();
      return Optional.of(companyEntity);
    }
    return Optional.empty();
  }
}
