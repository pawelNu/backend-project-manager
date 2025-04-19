package com.pawelnu.projectmanager.mapper;

import com.pawelnu.projectmanager.dto.project.ProjectCreateRequestDTO;
import com.pawelnu.projectmanager.dto.project.ProjectDTO;
import com.pawelnu.projectmanager.entity.ProjectEntity;
import com.pawelnu.projectmanager.enums.ProjectStatus;
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
