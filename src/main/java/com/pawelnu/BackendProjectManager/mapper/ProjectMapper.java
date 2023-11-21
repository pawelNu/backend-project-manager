package com.pawelnu.BackendProjectManager.mapper;

import com.pawelnu.BackendProjectManager.dto.ProjectCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.ProjectDTO;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.entity.enums.IsFinished;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDTO toDTO(ProjectEntity projectEntity);

    @Mapping(target = "id", ignore = true)
    ProjectEntity toEntity(ProjectCreateRequestDTO projectCreateRequest);

    default String isFinishedToString(IsFinished isFinished) {
        return isFinished.getValue();
    }

    default IsFinished toIsFinishedEnum(String projectStatus) {
        return IsFinished.fromValue(projectStatus);
    }
}
