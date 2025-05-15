package com.pawelnu.projectmanager.endpoints.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.CompanyRepository;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final CompanyRepository companyRepository;
  private final EmployeeQueryRepository employeeQueryRepository;
  private final EmployeeMapper employeeMapper;
  private final ObjectMapper objectMapper;
  private final PasswordEncoder passwordEncoder;

  public EmployeeDTO createEmployee(EmployeeCreateRequestDTO body) {
    Optional<CompanyEntity> company =
        companyRepository.findByIdAndIsDeletedFalse(body.getCompanyId());
    if (company.isPresent()) {
      body.setPassword(passwordEncoder.encode(body.getPassword()));
      EmployeeEntity entity = employeeMapper.toEntity(body);
      entity.setCompany(company.get());
      EmployeeEntity save = employeeRepository.save(entity);
      return employeeMapper.toDTO(save);
    } else {
      throw new NotFoundException(MSG.COMPANY_NOT_FOUND + body.getCompanyId());
    }
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
        employeeQueryRepository.getEmployeeList(filters, offset, limit, sortDir, sortField);
    List<EmployeeDTO> companyDTOs = page.getContent().stream().map(employeeMapper::toDTO).toList();

    long totalElements = page.getTotalElements();
    long end = Math.min(offset + limit - 1, totalElements - 1);
    String contentRange = Shared.prepareContentRange(offset, end, totalElements);
    return EmployeesListResponseDTO.builder()
        .data(companyDTOs)
        .contentRange(contentRange)
        .build();
  }

  public EmployeeDTO getEmployeeById(UUID id) {
    return employeeRepository
        .findById(id)
        .map(employeeMapper::toDTO)
        .orElseThrow(() -> new NotFoundException(MSG.EMPLOYEE_NOT_FOUND + id));
  }

  public EmployeeRowDTO findByUsernameWithAuthorities(String username) {
    List<EmployeeAuthorityRowDTO> rows =
        employeeQueryRepository.findByUsernameWithAuthorities(username);
    if (rows.isEmpty()) {
      throw new NotFoundException("User not found with username: " + username);
    } else {
      return employeeMapper.toEmployeeRowDTO(rows);
    }
  }

  public EmployeeDTO editById(UUID id, EmployeeEditRequestDTO body) {
    Optional<EmployeeEntity> employeeToEdit = employeeRepository.findById(id);
    if (employeeToEdit.isPresent()) {
      EmployeeEntity existingEmployee = employeeToEdit.get();
      employeeMapper.toEntity(body, existingEmployee);
      EmployeeEntity updatedEmployee = employeeRepository.save(existingEmployee);
      return employeeMapper.toDTO(updatedEmployee);
    } else {
      throw new NotFoundException(MSG.EMPLOYEE_NOT_FOUND + id);
    }
  }

  public SimpleResponse deleteById(UUID id) {
    Optional<EmployeeEntity> employeeToDelete = employeeRepository.findById(id);
    if (employeeToDelete.isPresent()) {
      employeeRepository.delete(employeeToDelete.get());
      return SimpleResponse.builder().message("Deleted employee with id: " + id).build();
    } else {
      throw new NotFoundException(MSG.EMPLOYEE_NOT_FOUND + id);
    }
  }
}
