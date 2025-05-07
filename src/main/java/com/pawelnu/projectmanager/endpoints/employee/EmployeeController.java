package com.pawelnu.projectmanager.endpoints.employee;

import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {
  private final EmployeeService employeeService;
  private final EmployeeRepository employeeRepository;

  @Override
  public ResponseEntity<EmployeeDTO> createEmployee(
      String authorizationHeader, EmployeeCreateRequestDTO body) {
    EmployeeDTO employee = employeeService.createEmployee(body);
    return ResponseEntity.ok(employee);
  }

  @Override
  public ResponseEntity<List<EmployeeDTO>> getEmployeeList(
      String authorizationHeader, String sort, String range, String filter) {
    EmployeesListResponseDTO result = employeeService.getEmployeeList(sort, range, filter);
    String contentRange =
        String.format(
            "items %d-%d/%d", result.getStart(), result.getEnd(), result.getTotalElements());
    return ResponseEntity.ok().header("Content-Range", contentRange).body(result.getData());
  }

  @Override
  public ResponseEntity<EmployeeDTO> getEmployeeById(String authorizationHeader, UUID id) {
    EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
    return ResponseEntity.ok(employeeDTO);
  }

  @Override
  public ResponseEntity<EmployeeDTO> editEmployeeById(
      String authorizationHeader, UUID id, EmployeeEditRequestDTO body) {
    //    TODO editEmployeeById
    return null;
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteEmployeeById(String authorizationHeader, UUID id) {
    //    TODO deleteEmployeeById
    return null;
  }
}
