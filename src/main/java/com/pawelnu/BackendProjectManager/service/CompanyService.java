package com.pawelnu.BackendProjectManager.service;

import com.pawelnu.BackendProjectManager.dto.company.CompanyCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.company.CompanyDTO;
import com.pawelnu.BackendProjectManager.dto.company.CompanyName;
import com.pawelnu.BackendProjectManager.dto.company.UpdateCompany;
import com.pawelnu.BackendProjectManager.dto.company.UpdatedCompanyDTO;
import com.pawelnu.BackendProjectManager.enums.CompanyStatus;
import java.util.List;
import java.util.UUID;

public interface CompanyService {

  List<CompanyName> getAllCompanies();

  List<CompanyDTO> getFilteredCompanies(String name, List<CompanyStatus> statuses);

  CompanyDTO createCompany(CompanyCreateRequestDTO companyCreateRequestDTO);

  UpdatedCompanyDTO updateCompany(UUID companyId, UpdateCompany updateCompany);

  String deleteCompany(UUID companyId);
}
