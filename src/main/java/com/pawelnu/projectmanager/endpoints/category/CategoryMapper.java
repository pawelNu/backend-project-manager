package com.pawelnu.projectmanager.endpoints.category;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  CategoryDTO toDTO(CategoryEntity companyEntity);

  CategoryEntity toEntity(CategoryCreateRequestDTO body);

  @Mapping(target = "id", ignore = true)
  CategoryEntity toEntity(
      CategoryEditRequestDTO body, @MappingTarget CategoryEntity existingCategory);
}
