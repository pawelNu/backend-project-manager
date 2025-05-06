package com.pawelnu.projectmanager.endpoints.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final EmployeeRepositoryQuery employeeRepositoryQuery;
  private final EmployeeMapper employeeMapper;
  private final ObjectMapper objectMapper;
  private static final String EMPLOYEE_NOT_FOUND_MSG = "Employee not found with id: ";

  public EmployeeDTO createEmployee(EmployeeCreateRequestDTO body) {
    EmployeeEntity entity = employeeMapper.toEntity(body);
    EmployeeEntity save = employeeRepository.save(entity);
    return employeeMapper.toDTO(save);
  }

  public EmployeesListResponseDTO getEmployeeList(String sort, String range, String filter) {

    List<String> sortList = Shared.parseJsonList(objectMapper, sort);
    String sortField = !sortList.isEmpty() ? sortList.get(0) : "lastName";
    String sortDir = sortList.size() > 1 ? sortList.get(1) : "ASC";

    List<Integer> rangeList = Shared.parseJsonListInt(objectMapper, range);
    int offset = !rangeList.isEmpty() ? rangeList.get(0) : 0;
    int limit = rangeList.size() > 1 ? rangeList.get(1) - rangeList.get(0) + 1 : 25;

    Map<String, String> filters = Shared.parseJsonMap(objectMapper, filter);

    Page<EmployeeEntity> page =
        employeeRepositoryQuery.getEmployeeList(filters, offset, limit, sortDir, sortField);
    List<EmployeeDTO> companyDTOs = page.getContent().stream().map(employeeMapper::toDTO).toList();

    long totalElements = page.getTotalElements();
    long end = Math.min(offset + limit - 1, totalElements - 1);

    return EmployeesListResponseDTO.builder()
        .data(companyDTOs)
        .totalElements(totalElements)
        .start(offset)
        .end(end)
        .build();
  }

  public EmployeeDTO getEmployeeById(UUID id) {
    return employeeRepository
        .findById(id)
        .map(employeeMapper::toDTO)
        .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND_MSG + id));
  }
}
