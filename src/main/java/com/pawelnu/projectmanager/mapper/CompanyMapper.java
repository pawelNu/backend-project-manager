package com.pawelnu.projectmanager.mapper;

import com.pawelnu.projectmanager.dto.company.CompanyCreateRequestDTO;
import com.pawelnu.projectmanager.dto.company.CompanyDTO;
import com.pawelnu.projectmanager.entity.CompanyEntity;
import com.pawelnu.projectmanager.enums.CompanyStatus;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

  CompanyDTO toDTO(CompanyEntity companyEntity);

  @Mapping(target = "id", ignore = true)
  CompanyEntity toEntity(CompanyCreateRequestDTO companyCreateRequest);

  List<CompanyDTO> toCompanyDTOList(List<CompanyEntity> companys);

  default String statusToString(CompanyStatus companyStatus) {
    return companyStatus.getValue();
  }

  default CompanyStatus statusToEnum(String companyStatus) {
    return CompanyStatus.fromValue(companyStatus);
  }
}
