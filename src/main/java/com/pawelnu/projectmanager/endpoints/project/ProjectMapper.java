package com.pawelnu.projectmanager.endpoints.project;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProjectMapper {
  @Mapping(target = "id", ignore = true)
  ProjectEntity toEntity(ProjectCreateRequestDTO companyCreateRequest);
}
