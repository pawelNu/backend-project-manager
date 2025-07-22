package com.pawelnu.projectmanager.endpoints.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.category.dto.CategoryCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.category.dto.CategoryDTO;
import com.pawelnu.projectmanager.endpoints.category.dto.CategoryEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.category.dto.CategoryListResponseDTO;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryQueryRepository categoryQueryRepository;
  private final CategoryMapper categoryMapper;
  private final CategoryMapperManual categoryMapperManual;
  private final ObjectMapper objectMapper;

  public CategoryDTO create(CategoryCreateRequestDTO body) {
    CategoryEntity entity = categoryMapper.toEntity(body);
    CategoryEntity save = categoryRepository.save(entity);
    return categoryMapper.toDTO(save);
  }

  public CategoryListResponseDTO filter(String sort, String range, String filter) {
    PageableParams params = Shared.preparePageableParams(objectMapper, sort, range, filter);

    Page<CategoryDTO> page =
        categoryQueryRepository.filter(
            params.getFilters(),
            params.getOffset(),
            params.getLimit(),
            params.getSortDir(),
            params.getSortField());
    List<CategoryDTO> companyDTOs = page.getContent();
    String contentRange = Shared.prepareContentRange(page, params.getOffset(), params.getLimit());
    return CategoryListResponseDTO.builder().data(companyDTOs).contentRange(contentRange).build();
  }

  public CategoryDTO getById(UUID id) {
    return categoryMapperManual.toDTO(getCategoryEntityById(id));
  }

  private CategoryEntity getCategoryEntityById(UUID id) {
    return categoryQueryRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(MSG.CATEGORY_NOT_FOUND_MSG + id));
  }

  public CategoryDTO editById(UUID id, CategoryEditRequestDTO body) {
    CategoryEntity categoryToEdit = getCategoryEntityById(id);
    categoryMapper.toEntity(body, categoryToEdit);
    CategoryEntity updatedCompany = categoryRepository.save(categoryToEdit);
    return categoryMapper.toDTO(updatedCompany);
  }

  public SimpleResponse deleteById(UUID id) {
    CategoryEntity categoryToDelete = getCategoryEntityById(id);
    categoryToDelete.setIsDeleted(true);
    CategoryEntity updatedCategory = categoryRepository.save(categoryToDelete);
    if (updatedCategory.getIsDeleted()) {
      return SimpleResponse.builder().message("Deleted category with id: " + id).build();
    } else {
      return SimpleResponse.builder().message("Cannot delete category with id: " + id).build();
    }
  }
}
