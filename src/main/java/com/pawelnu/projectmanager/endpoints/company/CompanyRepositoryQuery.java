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
    BooleanBuilder allConditions = new BooleanBuilder();
    if (body.getNames() != null && !body.getNames().isEmpty()) {
      BooleanBuilder namesCondition = new BooleanBuilder();
      for (String name : body.getNames()) {
        namesCondition.or(company.name.likeIgnoreCase("%" + name.toLowerCase() + "%"));
      }
      allConditions.or(namesCondition);
    }
    if (body.getNips() != null && !body.getNips().isEmpty()) {
      allConditions.or(company.nip.in(body.getNips()));
    }
    if (body.getRegons() != null && !body.getRegons().isEmpty()) {
      allConditions.or(company.regon.in(body.getRegons()));
    }
    Pageable pageable =
        Shared.preparePageable(
            body.getPage().getPageNumber(),
            body.getPage().getPageSize(),
            body.getPage().getSortedBy(),
            body.getPage().getDirection());
    return companyRepository.findAll(allConditions, pageable);
  }
}
