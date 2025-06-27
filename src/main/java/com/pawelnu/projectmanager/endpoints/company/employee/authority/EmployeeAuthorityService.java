package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import com.pawelnu.projectmanager.endpoints.authority.AuthorityEntity;
import com.pawelnu.projectmanager.endpoints.authority.AuthorityService;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeService;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeAuthorityService {

  private final EmployeeAuthorityRepository employeeAuthorityRepository;
  private final AuthorityService authorityService;
  private final EmployeeService employeeService;
  private final EmployeeAuthorityMapper employeeAuthorityMapper;

  public List<AddAuthorityToUserResponseDTO> addAuthoritiesToEmployee(
      UUID id, AddAuthorityToUserRequestDTO body) {
    List<AuthorityEntity> authorities =
        authorityService.findAllByIdInAndIsDeletedFalse(body.getAuthorityIds());
    EmployeeEntity employee = employeeService.getEmployeeEntityById(id);

    List<EmployeeAuthorityEntity> employeeAuthorities =
        authorities.stream()
            .map(
                authority ->
                    EmployeeAuthorityEntity.builder()
                        .authority(authority)
                        .employee(employee)
                        .build())
            .collect(Collectors.toList());

    List<EmployeeAuthorityEntity> savedEmployeeAuthorities =
        employeeAuthorityRepository.saveAll(employeeAuthorities);

    return savedEmployeeAuthorities.stream()
        .map(employeeAuthorityMapper::toDTO)
        .collect(Collectors.toList());
  }

  public SimpleResponse deleteAuthoritiesFromEmployee(
      UUID id, DeleteAuthorityFromUserRequestDTO body) {
    //    find EmployeeAuthority to delete
    //    delete authorities
    //    return response
    return null;
  }
}
