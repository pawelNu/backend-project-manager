package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import static com.pawelnu.projectmanager.utils.Consts.MSG.AUTHORITY_NOT_FOUND_MSG;
import static com.pawelnu.projectmanager.utils.Consts.MSG.EMPLOYEE_NOT_FOUND;

import com.pawelnu.projectmanager.endpoints.authority.AddAuthorityToUserRequestDTO;
import com.pawelnu.projectmanager.endpoints.authority.AddAuthorityToUserResponseDTO;
import com.pawelnu.projectmanager.endpoints.authority.AuthorityEntity;
import com.pawelnu.projectmanager.endpoints.authority.AuthorityRepository;
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

  public AddAuthorityToUserResponseDTO addAuthorityToEmployee(AddAuthorityToUserRequestDTO body) {
    AuthorityEntity authority =
        authorityRepository
            .findByIdAndIsDeletedFalse(body.getAuthorityId())
            .orElseThrow(
                () -> new NotFoundException(AUTHORITY_NOT_FOUND_MSG + body.getAuthorityId()));
    EmployeeEntity employee =
        employeeRepository
            .findByIdAndIsDeletedFalse(body.getEmployeeId())
            .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND + body.getEmployeeId()));
    EmployeeAuthorityEntity entity =
        EmployeeAuthorityEntity.builder().authority(authority).employee(employee).build();
    EmployeeAuthorityEntity save = employeeAuthorityRepository.save(entity);
    return employeeAuthorityMapper.toDTO(save);
  }
}
