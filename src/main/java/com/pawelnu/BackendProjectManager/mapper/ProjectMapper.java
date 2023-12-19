package com.pawelnu.BackendProjectManager.mapper;

import com.pawelnu.BackendProjectManager.dto.project.ProjectCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectDTO;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.entity.enums.Status;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDTO toDTO(ProjectEntity projectEntity);

    @Mapping(target = "id", ignore = true)
    ProjectEntity toEntity(ProjectCreateRequestDTO projectCreateRequest);

    List<ProjectDTO> toProjectDTOList(List<ProjectEntity> projects);

    default String statusToString(Status status) {
        return status.getValue();
    }

    default Status statusToEnum(String projectStatus) {
        return Status.fromValue(projectStatus);
    }
}
