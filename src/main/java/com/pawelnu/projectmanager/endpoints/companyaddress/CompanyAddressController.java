package com.pawelnu.projectmanager.endpoints.companyaddress;

import com.pawelnu.projectmanager.dto.SimpleResponse;
import com.pawelnu.projectmanager.endpoints.company.CompanyCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.company.CompanyDTO;
import com.pawelnu.projectmanager.endpoints.company.CompanyEditRequestDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyAddressController implements CompanyAddressApi {

  @Override
  public ResponseEntity<CompanyDTO> createCompanyAddress(
      String authorizationHeader, CompanyCreateRequestDTO body) {
    return null;
  }

  @Override
  public ResponseEntity<CompanyDTO> getCompanyAddressById(String authorizationHeader, UUID id) {
    return null;
  }

  @Override
  public ResponseEntity<CompanyDTO> editCompanyAddressById(
      String authorizationHeader, UUID id, CompanyEditRequestDTO body) {
    return null;
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteCompanyAddressById(
      String authorizationHeader, UUID id) {
    return null;
  }
}
