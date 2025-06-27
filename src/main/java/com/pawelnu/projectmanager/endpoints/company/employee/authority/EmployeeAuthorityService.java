package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import com.pawelnu.projectmanager.endpoints.authority.AuthorityEntity;
import com.pawelnu.projectmanager.endpoints.authority.AuthorityService;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeService;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
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

  public List<EmployeeAuthorityDTO> create(EmployeeAuthorityCreateRequestDTO body) {
    List<AuthorityEntity> authorities =
        authorityService.findAllByIdInAndIsDeletedFalse(body.getAuthorityIds());
    EmployeeEntity employee = employeeService.getEmployeeEntityById(body.getEmployeeId());

    List<EmployeeAuthorityEntity> employeeAuthorities =
        authorities.stream()
            .map(
                authority ->
                    EmployeeAuthorityEntity.builder()
                        .authority(authority)
                        .employee(employee)
                        .build())
            .toList();

    List<EmployeeAuthorityEntity> savedEmployeeAuthorities =
        employeeAuthorityRepository.saveAll(employeeAuthorities);

    return savedEmployeeAuthorities.stream().map(employeeAuthorityMapper::toDTO).toList();
  }

  public SimpleResponse deleteAuthoritiesFromEmployee(
      UUID employeeId, DeleteAuthorityFromUserRequestDTO body) {
    List<EmployeeAuthorityEntity> employeeAuthorities =
        findEmployeeAuthorityByEmployeeIdAndAuthorityIds(employeeId, body.getAuthorityIds());
    employeeAuthorities.forEach(employeeAuthority -> employeeAuthority.setIsDeleted(true));
    List<EmployeeAuthorityEntity> deletedEmployeeAuthorities =
        employeeAuthorityRepository.saveAll(employeeAuthorities);
    String message = validateDeleteResult(employeeId, deletedEmployeeAuthorities);
    return SimpleResponse.builder().message(message).build();
  }

  private static String validateDeleteResult(
      UUID employeeId, List<EmployeeAuthorityEntity> deletedEmployeeAuthorities) {
    List<String> messages =
        deletedEmployeeAuthorities.stream()
            .map(
                deleted -> {
                  String action = deleted.getIsDeleted() ? "Deleted" : "Cannot delete";
                  return action
                      + " employee authority with id: "
                      + deleted.getId()
                      + " for employee with id: "
                      + employeeId;
                })
            .toList();

    return String.join(", ", messages);
  }

  private List<EmployeeAuthorityEntity> findEmployeeAuthorityByEmployeeIdAndAuthorityIds(
      UUID employeeId, List<UUID> authorityIds) {
    List<EmployeeAuthorityEntity> employeeAuthorities =
        employeeAuthorityRepository.findAllByAuthority_IdInAndEmployee_IdAndIsDeletedFalse(
            authorityIds, employeeId);
    if (employeeAuthorities.isEmpty()) {
      String idsString =
          authorityIds.stream().map(UUID::toString).collect(Collectors.joining(", "));
      throw new NotFoundException(
          MSG.EMPLOYEE_AUTHORITIES_NOT_FOUND_MSG + String.join(", ", idsString));
    } else {
      return employeeAuthorities;
    }
  }
}
