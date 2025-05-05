package com.pawelnu.projectmanager.endpoints.companyaddress;

import com.pawelnu.projectmanager.endpoints.company.QCompanyEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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
public class CompanyAddressRepositoryQuery {

  private final CompanyAddressRepository companyAddressRepository;
  private final JPAQueryFactory queryFactory;

  public Page<CompanyAddressEntity> filterCompanies(
      Map<String, String> filters, int offset, int limit, String sortDir, String sortField) {
    log.info(filters.toString());
    QCompanyAddressEntity address = QCompanyAddressEntity.companyAddressEntity;
    QCompanyEntity company = QCompanyEntity.companyEntity;
    BooleanBuilder allConditions = new BooleanBuilder();

    if (filters.containsKey("companyName")) {
      allConditions.and(company.name.likeIgnoreCase("%" + filters.get("companyName") + "%"));
    }
    if (filters.containsKey(address.street.getMetadata().getName())) {
      allConditions.and(
          address.street.likeIgnoreCase(
              "%" + filters.get(address.street.getMetadata().getName()) + "%"));
    }
    if (filters.containsKey(address.streetNumber.getMetadata().getName())) {
      allConditions.and(
          address.streetNumber.eq(filters.get(address.streetNumber.getMetadata().getName())));
    }
    if (filters.containsKey(address.city.getMetadata().getName())) {
      allConditions.and(
          address.city.likeIgnoreCase(
              "%" + filters.get(address.city.getMetadata().getName()) + "%"));
    }
    if (filters.containsKey(address.zipCode.getMetadata().getName())) {
      allConditions.and(address.zipCode.eq(filters.get(address.zipCode.getMetadata().getName())));
    }
    if (filters.containsKey(address.country.getMetadata().getName())) {
      allConditions.and(
          address.country.likeIgnoreCase(
              "%" + filters.get(address.country.getMetadata().getName()) + "%"));
    }
    if (filters.containsKey(address.phoneNumber.getMetadata().getName())) {
      allConditions.and(
          address.phoneNumber.eq(filters.get(address.phoneNumber.getMetadata().getName())));
    }
    if (filters.containsKey(address.emailAddress.getMetadata().getName())) {
      allConditions.and(
          address.emailAddress.eq(filters.get(address.emailAddress.getMetadata().getName())));
    }
    if (filters.containsKey(address.addressType.getMetadata().getName())) {
      allConditions.and(
          address.addressType.eq(filters.get(address.addressType.getMetadata().getName())));
    }

    JPAQuery<CompanyAddressEntity> query =
        queryFactory.selectFrom(address)
            .leftJoin(address.company, company)
            .fetchJoin()
            .where(allConditions)
            .offset(offset)
            .limit(limit);

    if (!sortField.isEmpty()) {
      if (sortField.equals("companyName")) {
        query.orderBy(sortDir.equalsIgnoreCase("DESC") ? company.name.desc() : company.name.asc());
      } else {
        PathBuilder<CompanyAddressEntity> entityPath =
            new PathBuilder<>(CompanyAddressEntity.class, "companyAddressEntity");
        query.orderBy(
            new OrderSpecifier<>(
                Sort.Direction.fromString(sortDir) == Sort.Direction.ASC ? Order.ASC : Order.DESC,
                entityPath.getString(sortField)));
      }
    }

    List<CompanyAddressEntity> results = query.fetch();
    long total =
        Optional.ofNullable(
                queryFactory.select(address.count())
                    .from(address)
                    .leftJoin(address.company, company)
                    .where(allConditions)
                    .fetchOne())
            .orElse(0L);

    return new PageImpl<>(results, PageRequest.of(offset / limit, limit), total);
  }
}
