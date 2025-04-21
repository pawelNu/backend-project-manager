package com.pawelnu.projectmanager.endpoints.company;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyController implements CompanyRestApi {

  private final CompanyService service;

  @Override
  public ResponseEntity<CompanyDTO> createCompany(
      String authorizationHeader, @Valid CompanyCreateRequestDTO companyCreateRequestDTO) {
    CompanyDTO company = service.createCompany(companyCreateRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(company);
  }

  @Override
  public ResponseEntity<CompanyListResponseDTO> getAllCompanies(
      String authorizationHeader,
      Integer pageNumber,
      Integer pageSize,
      String sortedBy,
      String direction) {
    return ResponseEntity.ok(service.getAllCompanies(pageNumber, pageSize, sortedBy, direction));
  }

  @Override
  public ResponseEntity<CompanyDTO> getCompanyById(String authorizationHeader, UUID id) {
    return ResponseEntity.ok(service.getCompanyById(id));
  }

  @Override
  public ResponseEntity<CompanyDTO> editCompanyById(
      String authorizationHeader, UUID id, CompanyEditRequestDTO body) {
    return ResponseEntity.ok(service.editCompanyById(id, body));
  }

  //  //  TODO delete company by id
  //  //  TODO add custom filters
  //    //    TODO add authentication
  //    //    TODO add authorization
  //    //    TODO add request logging
}
