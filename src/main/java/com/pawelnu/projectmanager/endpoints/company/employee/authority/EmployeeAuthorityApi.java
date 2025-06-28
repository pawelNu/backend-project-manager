package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Path;
import com.pawelnu.projectmanager.utils.ResponseErrors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Employee Authorities")
@RequestMapping(Path.API_EMPLOYEE_AUTHORITIES)
public interface EmployeeAuthorityApi {

  @ApiResponse(
      responseCode = "201",
      description = "Created",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = EmployeeAuthoritiesDTO.class)))
      })
  @ResponseErrors
  @PostMapping("")
  @Operation(description = "Add new employee authority.")
  ResponseEntity<EmployeeAuthoritiesDTO> create(
      @Valid @RequestBody EmployeeAuthorityCreateRequestDTO body);

  @Operation(
      description =
          "List employees authority with filtering, sorting and pagination (react-admin format)")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = EmployeeAuthorityDTO.class))))
  @ResponseErrors
  @GetMapping("")
  ResponseEntity<List<EmployeeAuthorityDTO>> getList(
      @Parameter(description = "Sort as JSON string, e.g. [\"title\",\"ASC\"]")
          @RequestParam(required = false)
          String sort,
      @Parameter(description = "Range as JSON string, e.g. [0, 24]") @RequestParam(required = false)
          String range,
      @Parameter(description = "Filter as JSON string, e.g. {\"title\":\"bar\"}")
          @RequestParam(required = false)
          String filter);

  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = SimpleResponse.class))
      })
  @ResponseErrors
  @DeleteMapping("")
  @Operation(description = "Delete employee authorities.")
  ResponseEntity<SimpleResponse> delete(@Valid @RequestBody EmployeeAuthorityDeleteRequestDTO body);
}
