package com.pawelnu.projectmanager.endpoints.authority;

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
  public ResponseEntity<AuthorityDTO> createAuthority(AuthorityCreateRequestDTO body) {
    AuthorityDTO authorityDTO = authorityService.createAuthority(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(authorityDTO);
  }

  @Override
  public ResponseEntity<List<AuthorityDTO>> getAuthorityList(
      String sort, String range, String filter) {
    return null;
  }

  @Override
  public ResponseEntity<AuthorityDTO> getAuthorityById(UUID id) {
    return null;
  }

  @Override
  public ResponseEntity<AuthorityDTO> editAuthorityById(UUID id, AuthorityEditRequestDTO body) {
    return null;
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteAuthorityById(UUID id) {
    return null;
  }

  @Override
  public ResponseEntity<AddAuthorityToUserResponseDTO> addAuthorityToUser(
      AddAuthorityToUserRequestDTO body) {
    AddAuthorityToUserResponseDTO response = employeeAuthorityService.addAuthorityToUser(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
