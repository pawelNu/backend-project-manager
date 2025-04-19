package com.pawelnu.projectmanager.service;

import com.pawelnu.projectmanager.dto.company.CompanyCreateRequestDTO;
import com.pawelnu.projectmanager.dto.company.CompanyDTO;
import com.pawelnu.projectmanager.dto.company.CompanyName;
import com.pawelnu.projectmanager.dto.company.UpdateCompany;
import com.pawelnu.projectmanager.dto.company.UpdatedCompanyDTO;
import com.pawelnu.projectmanager.enums.CompanyStatus;
import java.util.List;
import java.util.UUID;

public interface CompanyService {

  List<CompanyName> getAllCompanies();

  List<CompanyDTO> getFilteredCompanies(String name, List<CompanyStatus> statuses);

  CompanyDTO createCompany(CompanyCreateRequestDTO companyCreateRequestDTO);

  UpdatedCompanyDTO updateCompany(UUID companyId, UpdateCompany updateCompany);

  String deleteCompany(UUID companyId);
}
