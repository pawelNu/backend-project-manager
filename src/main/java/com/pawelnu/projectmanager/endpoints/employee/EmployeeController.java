package com.pawelnu.projectmanager.endpoints.employee;

import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {

  private final EmployeeService employeeService;

  @Override
  public ResponseEntity<EmployeeDTO> create(EmployeeCreateRequestDTO body) {
    EmployeeDTO employee = employeeService.createEmployee(body);
    return ResponseEntity.ok(employee);
  }

  @Override
  public ResponseEntity<List<EmployeeDTO>> getList(String sort, String range, String filter) {
    EmployeesListResponseDTO result = employeeService.getEmployeeList(sort, range, filter);
    String contentRange =
        String.format(
            "items %d-%d/%d", result.getStart(), result.getEnd(), result.getTotalElements());
    return ResponseEntity.ok().header("Content-Range", contentRange).body(result.getData());
  }

  @Override
  public ResponseEntity<EmployeeDTO> getById(UUID id) {
    EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
    return ResponseEntity.ok(employeeDTO);
  }

  @Override
  public ResponseEntity<EmployeeDTO> editById(UUID id, EmployeeEditRequestDTO body) {
    EmployeeDTO result = employeeService.editById(id, body);
    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteById(UUID id) {
    SimpleResponse result = employeeService.deleteById(id);
    return ResponseEntity.ok(result);
  }
}
