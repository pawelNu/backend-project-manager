package com.pawelnu.projectmanager.endpoints.authority;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AuthorityMapper {

  AuthorityDTO toDTO(AuthorityEntity companyEntity);

  AuthorityEntity toEntity(AuthorityCreateRequestDTO body);

  @Mapping(target = "id", ignore = true)
  AuthorityEntity toEntity(
      AuthorityEditRequestDTO body, @MappingTarget AuthorityEntity existingAuthority);
}
