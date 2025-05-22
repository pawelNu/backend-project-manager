package com.pawelnu.projectmanager.endpoints.company.address;

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

  private final CompanyAddressService companyAddressService;

  @Override
  public ResponseEntity<CompanyAddressDTO> create(CompanyAddressCreateRequestDTO body) {
    CompanyAddressDTO result = companyAddressService.create(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @Override
  public ResponseEntity<List<CompanyAddressDTO>> getList(String sort, String range, String filter) {
    CompanyAddressesListResponseDTO result = companyAddressService.filter(sort, range, filter);
    return ResponseEntity.ok()
        .header("Content-Range", result.getContentRange())
        .body(result.getData());
  }

  @Override
  public ResponseEntity<CompanyAddressDTO> getById(UUID id) {
    CompanyAddressDTO result = companyAddressService.getById(id);
    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<CompanyAddressDTO> editById(UUID id, CompanyAddressEditRequestDTO body) {
    CompanyAddressDTO result = companyAddressService.editById(id, body);
    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteById(UUID id) {
    SimpleResponse result = companyAddressService.deleteById(id);
    return ResponseEntity.ok(result);
  }
}
