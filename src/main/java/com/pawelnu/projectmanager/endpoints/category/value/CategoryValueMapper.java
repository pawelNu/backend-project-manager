package com.pawelnu.projectmanager.endpoints.category.value;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryValueMapper {

  @Mapping(source = "category.name", target = "categoryName")
  CategoryValueDTO toDTO(CategoryValueEntity companyValueEntity);

  @Mapping(target = "id", ignore = true)
  CategoryValueEntity toEntity(CategoryValueCreateRequestDTO body);

  @Mapping(target = "id", ignore = true)
  CategoryValueEntity toEntity(
      CategoryValueEditRequestDTO body, @MappingTarget CategoryValueEntity existingCategoryValue);
}
