package com.pawelnu.projectmanager.endpoints.company.address;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CompanyAddressMapper {

  @Mapping(source = "company.name", target = "companyName")
  CompanyAddressDTO toDTO(CompanyAddressEntity companyAddressEntity);

  @Mapping(target = "id", ignore = true)
  CompanyAddressEntity toEntity(CompanyAddressCreateRequestDTO body);

  @Mapping(target = "id", ignore = true)
  CompanyAddressEntity toEntity(
      CompanyAddressEditRequestDTO body, @MappingTarget CompanyAddressEntity entity);
}
