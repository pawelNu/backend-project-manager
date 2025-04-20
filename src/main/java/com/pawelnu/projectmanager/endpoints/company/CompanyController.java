package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.api.CompaniesApi;
import com.pawelnu.projectmanager.model.CompanyListResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyController implements CompaniesApi {

  private final CompanyService service;

  @Override
  public ResponseEntity<CompanyListResponseDTO> getAllCompanies(
      @NotNull @Valid Integer pageNumber,
      @NotNull @Valid Integer pageSize,
      @NotNull @Valid String sortingField,
      @NotNull @Valid Boolean isAscendingSorting) {
    return ResponseEntity.ok(
        service.getAllCompanies(pageNumber, pageSize, sortingField, isAscendingSorting));
  }
}
