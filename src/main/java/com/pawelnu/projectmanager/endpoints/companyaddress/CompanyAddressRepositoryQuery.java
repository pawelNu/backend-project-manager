package com.pawelnu.projectmanager.endpoints.companyaddress;

import com.querydsl.core.BooleanBuilder;
import java.util.Map;
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
public class CompanyAddressRepositoryQuery {

  private final CompanyAddressRepository companyAddressRepository;

  public Page<CompanyAddressEntity> filterCompanies(
      Map<String, String> filters, int offset, int limit, String sortDir, String sortField) {
    log.info(filters.toString());
    QCompanyAddressEntity company = QCompanyAddressEntity.companyAddressEntity;
    BooleanBuilder allConditions = new BooleanBuilder();

    if (filters.containsKey(company.street.getMetadata().getName())) {
      allConditions.and(
          company.street.likeIgnoreCase(
              "%" + filters.get(company.street.getMetadata().getName()) + "%"));
    }
    if (filters.containsKey(company.streetNumber.getMetadata().getName())) {
      allConditions.and(
          company.streetNumber.eq(filters.get(company.streetNumber.getMetadata().getName())));
    }
    if (filters.containsKey(company.city.getMetadata().getName())) {
      allConditions.and(
          company.city.likeIgnoreCase(
              "%" + filters.get(company.city.getMetadata().getName()) + "%"));
    }
    if (filters.containsKey(company.zipCode.getMetadata().getName())) {
      allConditions.and(company.zipCode.eq(filters.get(company.zipCode.getMetadata().getName())));
    }
    if (filters.containsKey(company.country.getMetadata().getName())) {
      allConditions.and(
          company.country.likeIgnoreCase(
              "%" + filters.get(company.country.getMetadata().getName()) + "%"));
    }
    if (filters.containsKey(company.phoneNumber.getMetadata().getName())) {
      allConditions.and(
          company.phoneNumber.eq(filters.get(company.phoneNumber.getMetadata().getName())));
    }
    if (filters.containsKey(company.emailAddress.getMetadata().getName())) {
      allConditions.and(
          company.emailAddress.eq(filters.get(company.emailAddress.getMetadata().getName())));
    }
    if (filters.containsKey(company.addressType.getMetadata().getName())) {
      allConditions.and(
          company.addressType.eq(filters.get(company.addressType.getMetadata().getName())));
    }

    Pageable pageable =
        PageRequest.of(
            offset / limit, limit, Sort.by(Sort.Direction.fromString(sortDir), sortField));
    return companyAddressRepository.findAll(allConditions, pageable);
  }
}
