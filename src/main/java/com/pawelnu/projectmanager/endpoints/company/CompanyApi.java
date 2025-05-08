package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.Request;
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
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Companies")
@RequestMapping(Path.API_COMPANIES)
public interface CompanyApi {

  @ApiResponse(
      responseCode = "201",
      description = "Created",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = CompanyDTO.class))
      })
  @ResponseErrors
  @PostMapping("")
  @Operation(description = "Add new company.")
  ResponseEntity<CompanyDTO> create(@Valid @RequestBody CompanyCreateRequestDTO body);

  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = CompanyListResponseDTO.class))
      })
  @ResponseErrors
  @GetMapping("/pagination")
  @Operation(description = "Get list of companies.")
  ResponseEntity<CompanyListResponseDTO> getAllCompanies(
      @Parameter(description = "Page number from 0")
          @RequestParam(
              required = false,
              name = Request.PAGE_NUMBER_NAME,
              defaultValue = Request.PAGE_NUMBER_STRING)
          Integer pageNumber,
      @Parameter(description = "Page size")
          @RequestParam(
              required = false,
              name = Request.PAGE_SIZE_NAME,
              defaultValue = Request.PAGE_SIZE_NUMBER_STRING)
          Integer pageSize,
      @Parameter(description = "Sorting field name")
          @RequestParam(required = false, name = Request.SORTED_BY_NAME)
          String sortedBy,
      @Parameter(description = "Sorting direction [asc, desc, other - not sorted]")
          @RequestParam(required = false, name = Request.DIRECTION_NAME)
          String direction);

  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = CompanyListResponseDTO.class))
      })
  @ResponseErrors
  @PostMapping("/filter")
  @Operation(description = "Filter companies")
  ResponseEntity<CompanyListResponseDTO> filterCompanies(
      @Valid @RequestBody CompanyFilterRequestDTO body);

  @Operation(
      description = "List companies with filtering, sorting and pagination (react-admin format)")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = CompanySimpleDTO.class))))
  @ResponseErrors
  @GetMapping("")
  ResponseEntity<List<CompanySimpleDTO>> getList(
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
            schema = @Schema(implementation = CompanyDTO.class))
      })
  @ResponseErrors
  @GetMapping("/{id}")
  @Operation(description = "Get company by id.")
  ResponseEntity<CompanyDTO> getById(@Parameter() @PathVariable() UUID id);

  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = CompanyDTO.class))
      })
  @ResponseErrors
  @PutMapping("/{id}")
  @Operation(description = "Edit company by id.")
  ResponseEntity<CompanyDTO> editById(
      @Parameter() @PathVariable() UUID id, @Valid @RequestBody CompanyEditRequestDTO body);

  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = SimpleResponse.class))
      })
  @ResponseErrors
  @DeleteMapping("/{id}")
  @Operation(description = "Delete company by id.")
  ResponseEntity<SimpleResponse> deleteById(@Parameter() @PathVariable() UUID id);
}
