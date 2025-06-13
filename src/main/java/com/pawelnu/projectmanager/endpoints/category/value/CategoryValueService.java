package com.pawelnu.projectmanager.endpoints.category.value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    throw new UnsupportedOperationException("Not implemented yet");
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
