package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.utils.Shared;
import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CompanyRepositoryQuery {

  private CompanyRepository companyRepository;

  public Page<CompanyEntity> filterCompanies(CompanyFilterRequestDTO body) {
    QCompanyEntity company = QCompanyEntity.companyEntity;
    BooleanBuilder predicate = new BooleanBuilder();
    if (body.getName() != null) {
      predicate.or(company.name.likeIgnoreCase("%"+body.getName().toLowerCase()+"%"));
    }
    if (body.getNip() != null) {
      predicate.or(company.nip.eq(body.getNip()));
    }
    if (body.getRegon() != null) {
      predicate.or(company.regon.eq(body.getRegon()));
    }
    Pageable pageable =
        Shared.preparePageable(
            body.getPage().getPageNumber(),
            body.getPage().getPageSize(),
            body.getPage().getSortedBy(),
            body.getPage().getDirection());
    return companyRepository.findAll(predicate, pageable);
  }
}
