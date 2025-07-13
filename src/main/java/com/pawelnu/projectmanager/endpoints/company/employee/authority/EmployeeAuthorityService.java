package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.authority.AuthorityEntity;
import com.pawelnu.projectmanager.endpoints.authority.AuthorityService;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeService;
import com.pawelnu.projectmanager.endpoints.company.employee.authority.dto.EmployeeAuthorityCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.company.employee.authority.dto.EmployeeAuthorityCreateResponseDTO;
import com.pawelnu.projectmanager.endpoints.company.employee.authority.dto.EmployeeAuthorityDTO;
import com.pawelnu.projectmanager.endpoints.company.employee.authority.dto.EmployeeAuthorityDeleteRequestDTO;
import com.pawelnu.projectmanager.endpoints.company.employee.authority.dto.EmployeeAuthorityListResponseDTO;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeAuthorityService {

  private final EmployeeAuthorityRepository employeeAuthorityRepository;
  private final EmployeeAuthorityQueryRepository employeeAuthorityQueryRepository;
  private final AuthorityService authorityService;
  private final EmployeeService employeeService;
  private final EmployeeAuthorityMapper employeeAuthorityMapper;
  private final ObjectMapper objectMapper;

  public EmployeeAuthorityCreateResponseDTO create(EmployeeAuthorityCreateRequestDTO body) {
    List<UUID> authorityIds = body.getAuthorityIds();
    List<UUID> foundEmployeeAuthorities =
        employeeAuthorityRepository.findExistingAuthorityIdsForEmployee(
            body.getEmployeeId(), authorityIds);
    List<UUID> filteredAuthorityIds =
        authorityIds.stream().filter(uuid -> !foundEmployeeAuthorities.contains(uuid)).toList();
    List<AuthorityEntity> authorities =
        authorityService.findAllByIdInAndIsDeletedFalse(filteredAuthorityIds);
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
    return employeeAuthorityMapper.toDTO(savedEmployeeAuthorities);
  }

  public SimpleResponse delete(EmployeeAuthorityDeleteRequestDTO body) {
    List<EmployeeAuthorityEntity> employeeAuthorities =
        findEmployeeAuthorityByEmployeeIdAndAuthorityIds(
            body.getEmployeeId(), body.getAuthorityIds());
    employeeAuthorities.forEach(employeeAuthority -> employeeAuthority.setIsDeleted(true));
    List<EmployeeAuthorityEntity> deletedEmployeeAuthorities =
        employeeAuthorityRepository.saveAll(employeeAuthorities);
    String message = validateDeleteResult(body.getEmployeeId(), deletedEmployeeAuthorities);
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

  public EmployeeAuthorityListResponseDTO getList(String sort, String range, String filter) {
    PageableParams params =
        Shared.preparePageableParams(objectMapper, "employee_id", sort, range, filter);

    Page<EmployeeAuthorityEntity> page =
        employeeAuthorityQueryRepository.filter(
            params.getFilters(),
            params.getOffset(),
            params.getLimit(),
            params.getSortDir(),
            params.getSortField());
    List<EmployeeAuthorityDTO> employeeAuthorityDTOs =
        page.getContent().stream().map(employeeAuthorityMapper::toDTO).toList();
    String contentRange = Shared.prepareContentRange(page, params.getOffset(), params.getLimit());
    return EmployeeAuthorityListResponseDTO.builder()
        .data(employeeAuthorityDTOs)
        .contentRange(contentRange)
        .build();
  }

  public SimpleResponse deleteById(UUID id) {
    Optional<EmployeeAuthorityEntity> employeeToDelete =
        employeeAuthorityRepository.findByIdAndIsDeletedFalse(id);
    if (employeeToDelete.isPresent()) {
      EmployeeAuthorityEntity existingEmployee = employeeToDelete.get();
      existingEmployee.setIsDeleted(true);
      EmployeeAuthorityEntity updatedEmployee = employeeAuthorityRepository.save(existingEmployee);
      if (updatedEmployee.getIsDeleted()) {
        return SimpleResponse.builder()
            .message("Deleted employee authority with id: " + id)
            .build();
      } else {
        return SimpleResponse.builder()
            .message("Cannot delete employee authority with id: " + id)
            .build();
      }
    } else {
      throw new NotFoundException(MSG.EMPLOYEE_AUTHORITY_NOT_FOUND_MSG + id);
    }
  }
}
