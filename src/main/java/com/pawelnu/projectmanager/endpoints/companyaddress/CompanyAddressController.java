package com.pawelnu.projectmanager.endpoints.companyaddress;

import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyAddressController implements CompanyAddressApi {

  private final CompanyAddressService service;

  @Override
  public ResponseEntity<CompanyAddressDTO> create(
      String authorizationHeader, CompanyAddressCreateRequestDTO body) {
    CompanyAddressDTO result = service.createCompany(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @Override
  public ResponseEntity<List<CompanyAddressDTO>> getList(
      String authorizationHeader, String sort, String range, String filter) {
    CompanyAddressesListResponseDTO result = service.filterCompanies(sort, range, filter);
    String contentRange =
        String.format(
            "items %d-%d/%d", result.getStart(), result.getEnd(), result.getTotalElements());
    return ResponseEntity.ok().header("Content-Range", contentRange).body(result.getData());
  }

  @Override
  public ResponseEntity<CompanyAddressDTO> getById(String authorizationHeader, UUID id) {
    CompanyAddressDTO result = service.getCompanyById(id);
    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<CompanyAddressDTO> editById(
      String authorizationHeader, UUID id, CompanyAddressEditRequestDTO body) {
    CompanyAddressDTO result = service.editCompanyById(id, body);
    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteById(String authorizationHeader, UUID id) {
    SimpleResponse result = service.deleteCompanyById(id);
    return ResponseEntity.ok(result);
  }
}
