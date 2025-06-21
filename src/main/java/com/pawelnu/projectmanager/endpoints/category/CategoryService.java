package com.pawelnu.projectmanager.endpoints.category;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryQueryRepository categoryQueryRepository;
  private final CategoryMapper categoryMapper;
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
    return categoryQueryRepository
        .findById(id)
        .map(categoryMapper::toDTO)
        .orElseThrow(() -> new NotFoundException(MSG.AUTHORITY_NOT_FOUND_MSG + id));
  }

  public CategoryDTO editById(UUID id, CategoryEditRequestDTO body) {
    Optional<CategoryEntity> companyToEdit = categoryQueryRepository.findById(id);
    if (companyToEdit.isPresent()) {
      CategoryEntity existingAuthority = companyToEdit.get();
      categoryMapper.toEntity(body, existingAuthority);
      CategoryEntity updatedCompany = categoryRepository.save(existingAuthority);
      return categoryMapper.toDTO(updatedCompany);
    } else {
      throw new NotFoundException(MSG.AUTHORITY_NOT_FOUND_MSG + id);
    }
  }

  public SimpleResponse deleteById(UUID id) {
    Optional<CategoryEntity> authorityToDelete = categoryRepository.findByIdAndIsDeletedFalse(id);
    if (authorityToDelete.isPresent()) {
      CategoryEntity existingAuthority = authorityToDelete.get();
      existingAuthority.setIsDeleted(true);
      CategoryEntity updatedAuthority = categoryRepository.save(existingAuthority);
      if (updatedAuthority.getIsDeleted()) {
        return SimpleResponse.builder().message("Deleted authority with id: " + id).build();
      } else {
        return SimpleResponse.builder().message("Cannot delete authority with id: " + id).build();
      }
    } else {
      throw new NotFoundException(MSG.AUTHORITY_NOT_FOUND_MSG + id);
    }
  }
}
