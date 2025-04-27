package com.pawelnu.projectmanager.endpoints.companyaddress;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyAddressMapper {

  CompanyAddressDTO toDTO(CompanyAddressEntity companyAddressEntity);
}
