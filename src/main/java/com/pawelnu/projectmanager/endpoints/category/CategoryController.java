package com.pawelnu.projectmanager.endpoints.category;

import com.pawelnu.projectmanager.endpoints.authority.employee.EmployeeAuthorityService;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {

  private final CategoryService categoryService;
  private final EmployeeAuthorityService employeeAuthorityService;

  @Override
  public ResponseEntity<CategoryDTO> create(CategoryCreateRequestDTO body) {
    CategoryDTO categoryDTO = categoryService.create(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
  }

  @Override
  public ResponseEntity<List<CategoryDTO>> getList(String sort, String range, String filter) {
    CategoryListResponseDTO result = categoryService.filter(sort, range, filter);
    return ResponseEntity.ok()
        .header("Content-Range", result.getContentRange())
        .body(result.getData());
  }

  @Override
  public ResponseEntity<CategoryDTO> getById(UUID id) {
    return ResponseEntity.ok(categoryService.getById(id));
  }

  @Override
  public ResponseEntity<CategoryDTO> editById(UUID id, CategoryEditRequestDTO body) {
    return ResponseEntity.ok(categoryService.editById(id, body));
  }

  @Override
  public ResponseEntity<SimpleResponse> deleteById(UUID id) {
    return ResponseEntity.ok(categoryService.deleteById(id));
  }
}
