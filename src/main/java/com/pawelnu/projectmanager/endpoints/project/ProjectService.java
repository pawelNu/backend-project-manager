package com.pawelnu.projectmanager.endpoints.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueService;
import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.CompanyService;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final ProjectQueryRepository projectQueryRepository;
  private final CategoryValueService categoryValueService;
  private final CompanyService companyService;
  private final EmployeeService employeeService;
  private final ProjectMapper projectMapper;
  private final ObjectMapper objectMapper;

  public ProjectDTO create(ProjectCreateRequestDTO body) {
    CategoryValueEntity projectCategory =
        categoryValueService.findCategoryByNameAndValue("project category", "production");
    CompanyEntity companyEntity = companyService.getCompanyEntityById(body.getCompanyId());
    EmployeeEntity employeeEntity =
        employeeService.getEmployeeEntityById(body.getAssignedEmployeeId());
    CategoryValueEntity projectPriority =
        categoryValueService.findCategoryByNameAndValue("project priority", "5");
    ProjectEntity projectEntity = projectMapper.toEntity(body);
    projectEntity.setCategory(projectCategory);
    projectEntity.setCompany(companyEntity);
    projectEntity.setAssignedEmployee(employeeEntity);
    projectEntity.setPriority(projectPriority);
    ProjectEntity savedCompany = projectRepository.save(projectEntity);
    return projectMapper.toDTO(savedCompany);
  }
}
