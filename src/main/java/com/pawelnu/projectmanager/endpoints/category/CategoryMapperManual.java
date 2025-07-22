package com.pawelnu.projectmanager.endpoints.category;

import com.pawelnu.projectmanager.endpoints.category.dto.CategoryDTO;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueDTO;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapperManual {
  private final CategoryValueMapper categoryValueMapper;

  public CategoryDTO toDTO(CategoryEntity entity) {
    List<CategoryValueDTO> values =
        entity.getValues().stream().map(categoryValueMapper::toDTO).toList();
    return CategoryDTO.builder().id(entity.getId()).name(entity.getName()).values(values).build();
  }
}
