package com.pawelnu.projectmanager.endpoints.project;

import com.pawelnu.projectmanager.endpoints.company.CompanyCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.company.CompanyDTO;
import com.pawelnu.projectmanager.endpoints.company.CompanyEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.company.CompanyFilterRequestDTO;
import com.pawelnu.projectmanager.endpoints.company.CompanyListResponseDTO;
import com.pawelnu.projectmanager.endpoints.company.CompanyListResponseDTO2;
import com.pawelnu.projectmanager.endpoints.company.CompanyService;
import com.pawelnu.projectmanager.endpoints.company.CompanySimpleDTO;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectController implements ProjectApi {

  private final CompanyService companyService;

  @Override
  public ResponseEntity<CompanyDTO> create(CompanyCreateRequestDTO companyCreateRequestDTO) {
    CompanyDTO company = companyService.create(companyCreateRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(company);
  }

  @Override
  public ResponseEntity<CompanyListResponseDTO> getList(
      Integer pageNumber, Integer pageSize, String sortedBy, String direction) {
    return ResponseEntity.ok(companyService.filter(pageNumber, pageSize, sortedBy, direction));
  }

  @Override
  public ResponseEntity<CompanyDTO> getById(UUID id) {
    return ResponseEntity.ok(companyService.getById(id));
  }

  @Override
  public ResponseEntity<CompanyDTO> editById(UUID id, CompanyEditRequestDTO body) {
    return ResponseEntity.ok(companyService.editById(id, body));
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteById(UUID id) {
    return ResponseEntity.ok(companyService.deleteById(id));
  }

  @Override
  public ResponseEntity<CompanyListResponseDTO> filterCompanies(CompanyFilterRequestDTO body) {
    return ResponseEntity.ok(companyService.filter(body));
  }

  @Override
  public ResponseEntity<List<CompanySimpleDTO>> getList(String sort, String range, String filter) {
    CompanyListResponseDTO2 result = companyService.filter(sort, range, filter);
    return ResponseEntity.ok()
        .header("Content-Range", result.getContentRange())
        .body(result.getData());
  }
}
