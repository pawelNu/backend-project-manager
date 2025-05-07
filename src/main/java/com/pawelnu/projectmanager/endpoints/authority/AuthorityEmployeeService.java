package com.pawelnu.projectmanager.endpoints.authority;

import static com.pawelnu.projectmanager.endpoints.authority.AuthorityService.AUTHORITY_NOT_FOUND_MSG;
import static com.pawelnu.projectmanager.endpoints.employee.EmployeeService.EMPLOYEE_NOT_FOUND_MSG;

import com.pawelnu.projectmanager.endpoints.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.employee.EmployeeRepository;
import com.pawelnu.projectmanager.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityEmployeeService {

  private final AuthorityEmployeeRepository authorityEmployeeRepository;
  private final AuthorityRepository authorityRepository;
  private final EmployeeRepository employeeRepository;
  private final AuthorityEmployeeMapper authorityEmployeeMapper;

  public AddAuthorityToUserResponseDTO addAuthorityToUser(AddAuthorityToUserRequestDTO body) {
    AuthorityEntity authority =
        authorityRepository
            .findById(body.getAuthorityId())
            .orElseThrow(
                () -> new NotFoundException(AUTHORITY_NOT_FOUND_MSG + body.getAuthorityId()));
    EmployeeEntity employee =
        employeeRepository
            .findById(body.getUserId())
            .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND_MSG + body.getUserId()));
    AuthorityEmployeeEntity entity =
        AuthorityEmployeeEntity.builder().authority(authority).employee(employee).build();
    AuthorityEmployeeEntity save = authorityEmployeeRepository.save(entity);
    return authorityEmployeeMapper.toDTO(save);
  }
}
