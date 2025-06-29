package com.pawelnu.projectmanager.endpoints.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueService;
import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.CompanyService;
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
  private final ProjectMapper projectMapper;
  private final ObjectMapper objectMapper;

  public ProjectDTO create(ProjectCreateRequestDTO body) {
    String categoryString = "project category";
    String valueString = "production";
    //   TODO get from database categoryId;
    //   TODO get from database companyId;
    //   TODO get from database assignedEmployeeId;
    //   TODO get from database priorityId;
    CategoryValueEntity projectCategory =
        categoryValueService.findCategoryByNameAndValue(categoryString, valueString);
    CompanyEntity companyEntity = companyService.getCompanyEntityById(body.getCompanyId());
    companyService.getById(body.getCompanyId());
    ProjectEntity projectEntity = projectMapper.toEntity(body);
    projectEntity.setCategory(projectCategory);
    projectEntity.setCompany(companyEntity);
    projectEntity.setAssignedEmployee(projectCategory);
    projectEntity.setPriority(projectCategory);
    ProjectEntity savedCompany = projectRepository.save(projectEntity);
    return projectMapper.toDTO(savedCompany);
    throw new NotImplementedException("not implemented");
  }
}
