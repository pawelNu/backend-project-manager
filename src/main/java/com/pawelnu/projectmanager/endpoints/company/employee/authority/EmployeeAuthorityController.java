package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
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
    EmployeeAuthorityListResponseDTO result = employeeAuthorityService.getList(sort, range, filter);
    return ResponseEntity.ok()
        .header("Content-Range", result.getContentRange())
        .body(result.getData());
  }

  @Override
  public ResponseEntity<SimpleResponse> delete(EmployeeAuthorityDeleteRequestDTO body) {
    SimpleResponse result = employeeAuthorityService.delete(body);
    return ResponseEntity.ok(result);
  }
}
