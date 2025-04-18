package com.pawelnu.BackendProjectManager.mapper;

import com.pawelnu.BackendProjectManager.dto.project.ProjectCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectDTO;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.enums.ProjectStatus;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

  ProjectDTO toDTO(ProjectEntity projectEntity);

  @Mapping(target = "id", ignore = true)
  ProjectEntity toEntity(ProjectCreateRequestDTO projectCreateRequest);

  List<ProjectDTO> toProjectDTOList(List<ProjectEntity> projects);

  default String statusToString(ProjectStatus projectStatus) {
    return projectStatus.getValue();
  }

  default ProjectStatus statusToEnum(String projectStatus) {
    return ProjectStatus.fromValue(projectStatus);
  }
}
