package com.pawelnu.projectmanager.endpoints.category.value;

import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryValueController implements CategoryValueApi {

  private final CategoryValueService categoryValueService;

  @Override
  public ResponseEntity<CategoryValueDTO> create(CategoryValueCreateRequestDTO body) {
    CategoryValueDTO categoryDTO = categoryValueService.create(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
  }

  @Override
  public ResponseEntity<List<CategoryValueDTO>> getList(String sort, String range, String filter) {
    CategoryValueListResponseDTO result = categoryValueService.filter(sort, range, filter);
    return ResponseEntity.ok()
        .header("Content-Range", result.getContentRange())
        .body(result.getData());
  }

  @Override
  public ResponseEntity<CategoryValueDTO> getById(UUID id) {
    return ResponseEntity.ok(categoryValueService.getById(id));
  }

  @Override
  public ResponseEntity<CategoryValueDTO> editById(UUID id, CategoryValueEditRequestDTO body) {
    return ResponseEntity.ok(categoryValueService.editById(id, body));
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteById(UUID id) {
    return ResponseEntity.ok(categoryValueService.deleteById(id));
  }
}
