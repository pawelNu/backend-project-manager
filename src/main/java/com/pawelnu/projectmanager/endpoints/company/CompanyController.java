package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.api.CompaniesApi;
import com.pawelnu.projectmanager.model.CompanyDTO;
import com.pawelnu.projectmanager.model.CompanyListResponseDTO;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyController implements CompaniesApi {

  private final CompanyService service;

  @Override
  public ResponseEntity<CompanyListResponseDTO> getAllCompanies(
      @Valid Integer pageNumber,
      @Valid Integer pageSize,
      @Valid String sortedBy,
      @Valid String direction) {
    return ResponseEntity.ok(service.getAllCompanies(pageNumber, pageSize, sortedBy, direction));
  }

  @Override
  public ResponseEntity<CompanyDTO> getCompanyById(UUID id) {
    //  //  TODO get company by id
    return null;
  }

  //  //  TODO post company
  //  //  TODO put company by id
  //  //  TODO delete company by id
  //  //  TODO add custom filters
}
