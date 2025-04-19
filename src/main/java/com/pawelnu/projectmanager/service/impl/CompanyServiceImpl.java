package com.pawelnu.projectmanager.service.impl;

import com.pawelnu.projectmanager.dto.company.CompanyCreateRequestDTO;
import com.pawelnu.projectmanager.dto.company.CompanyDTO;
import com.pawelnu.projectmanager.dto.company.CompanyName;
import com.pawelnu.projectmanager.dto.company.UpdateCompany;
import com.pawelnu.projectmanager.dto.company.UpdatedCompanyDTO;
import com.pawelnu.projectmanager.entity.CompanyEntity;
import com.pawelnu.projectmanager.enums.CompanyStatus;
import com.pawelnu.projectmanager.mapper.CompanyMapper;
import com.pawelnu.projectmanager.repository.CompanyRepository;
import com.pawelnu.projectmanager.service.CompanyService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

  private final CompanyMapper mapper;
  private final CompanyRepository repository;

  //  private final PagingAndSortingMapper pagingAndSortingMapper;

  @Override
  public List<CompanyName> getAllCompanies() {
    return null;
  }

  @Override
  public List<CompanyDTO> getFilteredCompanies(String name, List<CompanyStatus> statuses) {
    return null;
  }

  @Override
  public CompanyDTO createCompany(CompanyCreateRequestDTO companyCreateRequestDTO) {
    CompanyEntity companyEntity = mapper.toEntity(companyCreateRequestDTO);
    companyEntity.setStatus(CompanyStatus.ACTIVE);
    CompanyEntity savedCompany = repository.save(companyEntity);
    return mapper.toDTO(savedCompany);
  }

  @Override
  public UpdatedCompanyDTO updateCompany(UUID companyId, UpdateCompany updateCompany) {
    return null;
  }

  @Override
  public String deleteCompany(UUID companyId) {
    return null;
  }
}
