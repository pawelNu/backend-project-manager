package com.pawelnu.projectmanager.endpoints.category.value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
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
public class CategoryValueService {

  private final CategoryValueRepository categoryValueRepository;
  private final CategoryValueQueryRepository categoryValueQueryRepository;
  private final CategoryValueMapper categoryValueMapper;
  private final ObjectMapper objectMapper;

  public CategoryValueDTO create(CategoryValueCreateRequestDTO body) {
    CategoryValueEntity entity = categoryValueMapper.toEntity(body);
    CategoryValueEntity save = categoryValueRepository.save(entity);
    return categoryValueMapper.toDTO(save);
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
    throw new UnsupportedOperationException("Not implemented yet");
  }

  public CategoryValueDTO editById(UUID id, CategoryValueEditRequestDTO body) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  public SimpleResponse deleteById(UUID id) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
