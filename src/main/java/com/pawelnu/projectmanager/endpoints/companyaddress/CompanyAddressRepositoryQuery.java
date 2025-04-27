package com.pawelnu.projectmanager.endpoints.companyaddress;

import com.pawelnu.projectmanager.endpoints.company.QCompanyEntity;
import com.querydsl.core.BooleanBuilder;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CompanyAddressRepositoryQuery {

  private final CompanyAddressRepository companyAddressRepository;

  public Page<CompanyAddressEntity> filterCompanies(
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
    return companyAddressRepository.findAll(allConditions, pageable);
  }
}
