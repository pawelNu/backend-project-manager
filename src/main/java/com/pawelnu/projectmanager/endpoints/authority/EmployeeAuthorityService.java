package com.pawelnu.projectmanager.endpoints.authority;

import static com.pawelnu.projectmanager.endpoints.authority.AuthorityService.AUTHORITY_NOT_FOUND_MSG;
import static com.pawelnu.projectmanager.utils.Consts.MSG.EMPLOYEE_NOT_FOUND;

import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeRepository;
import com.pawelnu.projectmanager.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeAuthorityService {

  private final EmployeeAuthorityRepository employeeAuthorityRepository;
  private final AuthorityRepository authorityRepository;
  private final EmployeeRepository employeeRepository;
  private final EmployeeAuthorityMapper employeeAuthorityMapper;

  public AddAuthorityToUserResponseDTO addAuthorityToUser(AddAuthorityToUserRequestDTO body) {
    AuthorityEntity authority =
        authorityRepository
            .findById(body.getAuthorityId())
            .orElseThrow(
                () -> new NotFoundException(AUTHORITY_NOT_FOUND_MSG + body.getAuthorityId()));
    EmployeeEntity employee =
        employeeRepository
            .findById(body.getUserId())
            .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND + body.getUserId()));
    EmployeeAuthorityEntity entity =
        EmployeeAuthorityEntity.builder().authority(authority).employee(employee).build();
    EmployeeAuthorityEntity save = employeeAuthorityRepository.save(entity);
    return employeeAuthorityMapper.toDTO(save);
  }
}
