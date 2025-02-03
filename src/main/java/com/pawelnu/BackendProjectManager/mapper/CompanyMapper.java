package com.pawelnu.BackendProjectManager.mapper;

import com.pawelnu.BackendProjectManager.dto.company.CompanyCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.company.CompanyDTO;
import com.pawelnu.BackendProjectManager.entity.CompanyEntity;
import com.pawelnu.BackendProjectManager.enums.CompanyStatus;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

  CompanyDTO toDTO(CompanyEntity companyEntity);

  @Mapping(target = "id", ignore = true)
  CompanyEntity toEntity(CompanyCreateRequestDTO projectCreateRequest);

  List<CompanyDTO> toCompanyDTOList(List<CompanyEntity> projects);

  default String statusToString(CompanyStatus projectStatus) {
    return projectStatus.getValue();
  }

  default CompanyStatus statusToEnum(String projectStatus) {
    return CompanyStatus.fromValue(projectStatus);
  }
}
