package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmployeeAuthorityController implements EmployeeAuthorityApi {

  private final EmployeeAuthorityService employeeAuthorityService;

  @Override
  public ResponseEntity<List<EmployeeAuthorityDTO>> create(EmployeeAuthorityCreateRequestDTO body) {
    List<EmployeeAuthorityDTO> result = employeeAuthorityService.create(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @Override
  public ResponseEntity<List<EmployeeAuthorityDTO>> getList(
      String sort, String range, String filter) {
    return null;
  }

  @Override
  public ResponseEntity<EmployeeAuthorityDTO> getById(UUID id) {
    return null;
  }

  @Override
  public ResponseEntity<SimpleResponse> delete(DeleteAuthorityFromUserRequestDTO body) {
    return null;
  }
}
