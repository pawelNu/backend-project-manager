package com.pawelnu.projectmanager.endpoints.authority;

import com.pawelnu.projectmanager.endpoints.authority.dto.AuthorityCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.authority.dto.AuthorityDTO;
import com.pawelnu.projectmanager.endpoints.authority.dto.AuthorityEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.authority.dto.AuthorityListResponseDTO;
import com.pawelnu.projectmanager.endpoints.company.employee.authority.EmployeeAuthorityService;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthorityController implements AuthorityApi {

  private final AuthorityService authorityService;
  private final EmployeeAuthorityService employeeAuthorityService;

  @Override
  public ResponseEntity<AuthorityDTO> create(AuthorityCreateRequestDTO body) {
    AuthorityDTO authorityDTO = authorityService.create(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(authorityDTO);
  }

  @Override
  public ResponseEntity<List<AuthorityDTO>> getList(String sort, String range, String filter) {
    AuthorityListResponseDTO result = authorityService.filter(sort, range, filter);
    return ResponseEntity.ok()
        .header("Content-Range", result.getContentRange())
        .body(result.getData());
  }

  @Override
  public ResponseEntity<AuthorityDTO> getById(UUID id) {
    return ResponseEntity.ok(authorityService.getById(id));
  }

  @Override
  public ResponseEntity<AuthorityDTO> editById(UUID id, AuthorityEditRequestDTO body) {
    return ResponseEntity.ok(authorityService.editById(id, body));
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteById(UUID id) {
    return ResponseEntity.ok(authorityService.deleteById(id));
  }
}
