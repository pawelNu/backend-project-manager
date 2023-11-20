package com.pawelnu.BackendProjectManager.mapper;

import com.pawelnu.BackendProjectManager.dto.ProjectDTO;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.entity.enums.IsFinished;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

//    @Mapping(source = "isFinished", target = "isFinished")
    ProjectDTO toDTO(ProjectEntity projectEntity);

    default  String isFinishedToString(IsFinished isFinished) {
        return isFinished.getValue();
    }
}
