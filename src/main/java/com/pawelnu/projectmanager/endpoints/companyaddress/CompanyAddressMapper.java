package com.pawelnu.projectmanager.endpoints.companyaddress;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyAddressMapper {

  @Mapping(source = "company.name", target = "companyName")
  CompanyAddressDTO toDTO(CompanyAddressEntity companyAddressEntity);

  @Mapping(target = "id", ignore = true)
  CompanyAddressEntity toEntity(CompanyAddressCreateRequestDTO body);

  @Mapping(target = "id", ignore = true)
  CompanyAddressEntity toEntity(
      CompanyAddressEditRequestDTO companyEditRequestDTO,
      @MappingTarget CompanyAddressEntity companyEntity);
}
