package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.enums.CompanyStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

  @Mapping(target = "addresses", source = "addresses")
  CompanyDTO toDTO(CompanyEntity companyEntity);

  CompanySimpleDTO toSimpleDTO(CompanyEntity companyEntity);

  @Mapping(target = "id", ignore = true)
  CompanyEntity toEntity(CompanyCreateRequestDTO companyCreateRequest);

  @Mapping(target = "id", ignore = true)
  CompanyEntity toEntity(
      CompanyEditRequestDTO companyEditRequestDTO, @MappingTarget CompanyEntity companyEntity);

  //  List<CompanyDTO> toCompanyDTOList(List<CompanyEntity> companys);

  default String statusToString(CompanyStatus companyStatus) {
    return companyStatus.getValue();
  }

  default CompanyStatus statusToEnum(String companyStatus) {
    return CompanyStatus.fromValue(companyStatus);
  }
}
