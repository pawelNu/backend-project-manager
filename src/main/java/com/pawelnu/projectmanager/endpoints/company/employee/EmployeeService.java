package com.pawelnu.projectmanager.endpoints.company.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.CompanyService;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
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
  private final CompanyService companyService;
  private final EmployeeQueryRepository employeeQueryRepository;
  private final EmployeeMapper employeeMapper;
  private final ObjectMapper objectMapper;
  private final PasswordEncoder passwordEncoder;

  public EmployeeDTO createEmployee(EmployeeCreateRequestDTO body) {
    CompanyEntity company = companyService.getCompanyEntityById(body.getCompanyId());
    body.setPassword(passwordEncoder.encode(body.getPassword()));
    EmployeeEntity entity = employeeMapper.toEntity(body);
    entity.setCompany(company);
    EmployeeEntity save = employeeRepository.save(entity);
    return employeeMapper.toDTO(save);
  }

  public EmployeesListResponseDTO getEmployeeList(String sort, String range, String filter) {
    PageableParams params =
        Shared.preparePageableParams(objectMapper, "lastName", sort, range, filter);

    Page<EmployeeEntity> page =
        employeeQueryRepository.getEmployeeList(
            params.getFilters(),
            params.getOffset(),
            params.getLimit(),
            params.getSortDir(),
            params.getSortField());
    List<EmployeeDTO> companyDTOs = page.getContent().stream().map(employeeMapper::toDTO).toList();
    String contentRange = Shared.prepareContentRange(page, params.getOffset(), params.getLimit());
    return EmployeesListResponseDTO.builder().data(companyDTOs).contentRange(contentRange).build();
  }

  public EmployeeEntity getEmployeeEntityById(UUID id) {
    return employeeQueryRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(MSG.EMPLOYEE_NOT_FOUND + id));
  }

  public EmployeeDTO getEmployeeById(UUID id) {
    EmployeeEntity employee = getEmployeeEntityById(id);
    return employeeMapper.toDTO(employee);
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
    Optional<EmployeeEntity> employeeToDelete = employeeRepository.findByIdAndIsDeletedFalse(id);
    if (employeeToDelete.isPresent()) {
      EmployeeEntity existingEmployee = employeeToDelete.get();
      existingEmployee.setIsDeleted(true);
      EmployeeEntity updatedEmployee = employeeRepository.save(existingEmployee);
      if (updatedEmployee.getIsDeleted()) {
        return SimpleResponse.builder().message("Deleted employee with id: " + id).build();
      } else {
        return SimpleResponse.builder().message("Cannot delete employee with id: " + id).build();
      }
    } else {
      throw new NotFoundException(MSG.EMPLOYEE_NOT_FOUND + id);
    }
  }
}
