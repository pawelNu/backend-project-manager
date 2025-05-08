package com.pawelnu.projectmanager.endpoints.authority;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {

  AuthorityDTO toDTO(AuthorityEntity companyEntity);

  AuthorityEntity toEntity(AuthorityCreateRequestDTO body);
}
