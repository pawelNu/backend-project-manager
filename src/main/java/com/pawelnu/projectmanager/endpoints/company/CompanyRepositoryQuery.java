package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.endpoints.companyaddress.QCompanyAddressEntity;
import com.pawelnu.projectmanager.utils.Shared;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CompanyRepositoryQuery {

  private final CompanyRepository companyRepository;
  private final JPAQueryFactory queryFactory;

  public Page<CompanyEntity> filterCompanies(CompanyFilterRequestDTO body) {
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

  public Page<CompanyEntity> filterCompanies(
      Map<String, String> filters, int offset, int limit, String sortDir, String sortField) {
    QCompanyEntity company = QCompanyEntity.companyEntity;
    BooleanBuilder allConditions = new BooleanBuilder();

    if (filters.containsKey("name")) {
      allConditions.and(company.name.likeIgnoreCase("%" + filters.get("name") + "%"));
    }
    if (filters.containsKey("nip")) {
      allConditions.and(company.nip.eq(filters.get("nip")));
    }
    if (filters.containsKey("regon")) {
      allConditions.and(company.regon.eq(filters.get("regon")));
    }

    Pageable pageable =
        PageRequest.of(
            offset / limit, limit, Sort.by(Sort.Direction.fromString(sortDir), sortField));
    return companyRepository.findAll(allConditions, pageable);
  }

  public Optional<CompanyEntity> findById(UUID id) {
    QCompanyEntity company = QCompanyEntity.companyEntity;
    QCompanyAddressEntity address = QCompanyAddressEntity.companyAddressEntity;

    List<CompanyEntity> fetch =
        queryFactory
            .selectFrom(company)
            .innerJoin(company.addresses, address)
            .fetchJoin()
            .where(company.id.eq(id))
            .fetch();

    if (fetch != null && !fetch.isEmpty()) {
      CompanyEntity companyEntity = fetch.getFirst();
      return Optional.of(companyEntity);
    }
    return Optional.empty();
  }

}
