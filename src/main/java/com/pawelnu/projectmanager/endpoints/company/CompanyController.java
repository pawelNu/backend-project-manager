package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.dto.SimpleResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyController implements CompanyApi {

  private final CompanyService service;

  @Override
  public ResponseEntity<CompanyDTO> createCompany(
      String authorizationHeader, CompanyCreateRequestDTO companyCreateRequestDTO) {
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

  @Override
  public ResponseEntity<SimpleResponse> deleteCompanyById(String authorizationHeader, UUID id) {
    return ResponseEntity.ok(service.deleteCompanyById(id));
  }

  @Override
  public ResponseEntity<CompanyListResponseDTO> filterCompanies(
      String authorizationHeader, CompanyFilterRequestDTO body) {
    return ResponseEntity.ok(service.filterCompanies(body));
  }

  //    //    TODO add authentication
  //    //    TODO add authorization
  //    //    TODO add request logging
}
