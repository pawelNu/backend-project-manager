package com.pawelnu.projectmanager.endpoints.employee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {
  private final EmployeeRepository employeeRepository;
  private final EmployeeMapper employeeMapper;

  public EmployeeDTO createEmployee(EmployeeCreateRequestDTO body) {
    EmployeeEntity entity = employeeMapper.toEntity(body);
    EmployeeEntity save = employeeRepository.save(entity);
    return employeeMapper.toDTO(save);
  }
}
