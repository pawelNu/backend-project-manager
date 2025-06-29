package com.pawelnu.projectmanager.endpoints.category.value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.category.CategoryEntity;
import com.pawelnu.projectmanager.endpoints.category.CategoryRepository;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryValueService {

  private final CategoryValueRepository categoryValueRepository;
  private final CategoryRepository categoryRepository;
  private final CategoryValueQueryRepository categoryValueQueryRepository;
  private final CategoryValueMapper categoryValueMapper;
  private final ObjectMapper objectMapper;

  public CategoryValueDTO create(CategoryValueCreateRequestDTO body) {

    Optional<CategoryEntity> category =
        categoryRepository.findByIdAndIsDeletedFalse(body.getCategoryId());
    if (category.isPresent()) {
      CategoryValueEntity categoryValueEntity = categoryValueMapper.toEntity(body);
      categoryValueEntity.setCategory(category.get());
      CategoryValueEntity savedAddress = categoryValueRepository.save(categoryValueEntity);
      return categoryValueMapper.toDTO(savedAddress);
    } else {
      throw new NotFoundException(MSG.CATEGORY_NOT_FOUND_MSG + body.getCategoryId());
    }
  }

  public CategoryValueListResponseDTO filter(String sort, String range, String filter) {
    PageableParams params = Shared.preparePageableParams(objectMapper, sort, range, filter);

    Page<CategoryValueDTO> page =
        categoryValueQueryRepository.filter(
            params.getFilters(),
            params.getOffset(),
            params.getLimit(),
            params.getSortDir(),
            params.getSortField());
    List<CategoryValueDTO> categoryValueDTOs = page.getContent();
    String contentRange = Shared.prepareContentRange(page, params.getOffset(), params.getLimit());
    return CategoryValueListResponseDTO.builder()
        .data(categoryValueDTOs)
        .contentRange(contentRange)
        .build();
  }

  public CategoryValueDTO getById(UUID id) {
    return categoryValueMapper.toDTO(getCategoryValueById(id));
  }

  public CategoryValueEntity getCategoryValueById(UUID id) {
    return categoryValueQueryRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(MSG.CATEGORY_VALUE_NOT_FOUND_MSG + id));
  }

  public CategoryValueDTO editById(UUID id, CategoryValueEditRequestDTO body) {
    Optional<CategoryValueEntity> categoryValueToEdit = categoryValueQueryRepository.findById(id);
    if (categoryValueToEdit.isPresent()) {
      CategoryValueEntity existingCategoryValue = categoryValueToEdit.get();
      categoryValueMapper.toEntity(body, existingCategoryValue);
      CategoryValueEntity updatedCompany = categoryValueRepository.save(existingCategoryValue);
      return categoryValueMapper.toDTO(updatedCompany);
    } else {
      throw new NotFoundException(MSG.CATEGORY_VALUE_NOT_FOUND_MSG + id);
    }
  }

  public SimpleResponse deleteById(UUID id) {
    Optional<CategoryValueEntity> categoryToDelete =
        categoryValueRepository.findByIdAndIsDeletedFalse(id);
    if (categoryToDelete.isPresent()) {
      CategoryValueEntity existingCategoryValue = categoryToDelete.get();
      existingCategoryValue.setIsDeleted(true);
      CategoryValueEntity updatedCategoryValue =
          categoryValueRepository.save(existingCategoryValue);
      if (updatedCategoryValue.getIsDeleted()) {
        return SimpleResponse.builder().message("Deleted category value with id: " + id).build();
      } else {
        return SimpleResponse.builder()
            .message("Cannot delete category value with id: " + id)
            .build();
      }
    } else {
      throw new NotFoundException(MSG.CATEGORY_VALUE_NOT_FOUND_MSG + id);
    }
  }

  public CategoryValueEntity findCategoryByNameAndValue(String categoryString, String valueString) {
    return categoryValueQueryRepository
        .findCategoryByNameAndValue(categoryString, valueString)
        .orElseThrow(
            () ->
                new NotFoundException(
                    "For category: %s, category value: %s not found"
                        .formatted(categoryString, valueString)));
  }
}
