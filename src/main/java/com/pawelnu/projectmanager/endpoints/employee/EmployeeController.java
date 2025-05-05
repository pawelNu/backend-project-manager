package com.pawelnu.projectmanager.endpoints.employee;

import com.pawelnu.projectmanager.dto.SimpleResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {

  @Override
  public ResponseEntity<EmployeeDTO> createEmployee(
      String authorizationHeader, EmployeeCreateRequestDTO body) {
    return null;
  }

  @Override
  public ResponseEntity<List<EmployeeDTO>> getEmployeeList(
      String authorizationHeader, String sort, String range, String filter) {
    return null;
  }

  @Override
  public ResponseEntity<EmployeeDTO> getEmployeeById(String authorizationHeader, UUID id) {
    return null;
  }

  @Override
  public ResponseEntity<EmployeeDTO> editEmployeeById(
      String authorizationHeader, UUID id, EmployeeEditRequestDTO body) {
    return null;
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteEmployeeById(String authorizationHeader, UUID id) {
    return null;
  }
}
