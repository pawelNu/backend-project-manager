package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.api.CompaniesApi;
import com.pawelnu.projectmanager.model.CompanyListResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyController implements CompaniesApi {

  @Override
  public ResponseEntity<CompanyListResponseDTO> getAllCompanies(
      @Valid Integer pageNumber,
      @Valid Integer pageSize,
      @Valid String sortingField,
      @Valid Boolean isAscendingSorting) {
    return null;
  }
}
