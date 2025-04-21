package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.api.CompaniesApi;
import com.pawelnu.projectmanager.model.CompanyCreateRequestDTO;
import com.pawelnu.projectmanager.model.CompanyDTO;
import com.pawelnu.projectmanager.model.CompanyListResponseDTO;
import com.pawelnu.projectmanager.utils.ResponseErrors;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyController implements CompaniesApi {

  private final CompanyService service;

  @Override
  @ResponseErrors
  public ResponseEntity<CompanyDTO> createCompany(
      @Valid CompanyCreateRequestDTO companyCreateRequestDTO) {
    CompanyDTO company = service.createCompany(companyCreateRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(company);
  }

  @Override
  @ResponseErrors
  public ResponseEntity<CompanyListResponseDTO> getAllCompanies(
      @Valid Integer pageNumber,
      @Valid Integer pageSize,
      @Valid String sortedBy,
      @Valid String direction) {
    return ResponseEntity.ok(service.getAllCompanies(pageNumber, pageSize, sortedBy, direction));
  }

  @Override
  @ResponseErrors
  public ResponseEntity<CompanyDTO> getCompanyById(UUID id) {
    return ResponseEntity.ok(service.getCompanyById(id));
  }

  //  //  TODO post company
  //  //  TODO put company by id
  //  //  TODO delete company by id
  //  //  TODO add custom filters
}
